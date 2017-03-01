import java.util.List;
import java.util.ArrayList;

// Sparse Matrix class: 2D grid of linked nodes; elements which are
// the fillElem do not have nodes present. Retains a dense array of
// row and column pointers to speed some operations and ease
// implementation.
//
// Target Space Complexity: O(E + R + C)
//   E: number of non-fill elements in the matrix
//   R: number of rows in the matrix
//   C: number of cols in the matrix
public class SparseMatrix<T> {
    
    private ArrayList<Head<T>> theRows; // Each row is represented by a Head.
    private ArrayList<Head<T>> theCols; // Each column is represented by a Head.
    private T fillElem;
    private int size = 0;
    
    // Suggested internal class to represent Row and Column
    // Headers. Tracks indices of the col or row, pointer to the start
    // of row/column. This is a separat class from Node to enable
    // efficient changing of row numbers: all Nodes point to a row Head
    // and col Head to determine their own row/column. You may modify
    // this class as you see fit.
    protected static class Head<X> {
        
        public int index;             // Index of row/col
        
        public Node<X> nodes;         // Dummy node at start of row/column; add data nodes after the dummy
        
        // Constructor for a new Head of a node list, i is the row or column number for this Head
        public Head(int i) {
            this.index = i;
            nodes = new Node<X>();
        }
        
    }
    
    // Suggested class to store data. Contains single links to next
    // nodes to the right and down. Also contains links to the row and
    // col Heads which store its row# and col#.  You may modify this
    // class as you see fit.
    protected static class Node<Y> {
        
        public Head<Y> rowHead;     // My row head
        
        public Head<Y> colHead;     // My col head
        
        public Node<Y> right;       // Next Node to the right
        
        public Node<Y> down;        // Next node down
        
        public Y data;              // Data associated with the node
       
    }
    
    // Constructor to create a SparseMatrix with the given number of
    // rows and columns with the given fillElem. The matrix starts out
    // empty: all elements are presumed to be the fillElem.
    //
    // Target Complexity: O(R+C)
    //   R: number of rows in the matrix
    //   C: number of cols in the matrix
    public SparseMatrix(int r, int c, T fillElem) {
        theRows = new ArrayList<Head<T>>(r);
        // Create Heads to for the rows.
        for(int i = 0; i < r; i++) {
            theRows.add(new Head<T>(i));
        }
        theCols = new ArrayList<Head<T>>(c);
        // Creating Head for the columns.
        for(int i = 0; i < c; i++) {
            theCols.add(new Head<T>(i));
        }
        this.fillElem = fillElem;
    }
    
    // Constructor to create a 0 by 0 SparseMatrix with the given
    // fillElem
    public SparseMatrix(T fillElem) {
        theRows = new ArrayList<Head<T>>();
        theCols = new ArrayList<Head<T>>();
        this.fillElem = fillElem;
    }
    
    // Return the number of non-fill elements in the matrix which have
    // been explictily set. Corresponds to the number of non-dummy
    // nodes.
    // 
    // Target Complexity: O(1)
    public int elementCount() {
        return size;
    }
    
    // Return the number of rows in the Matrix which is the last indexed
    // row+1.
    // 
    // Target Complexity: O(1)
    public int rows() {
        return theRows.size();
    }
    
    // Return the number of cols in the Matrix which is the last indexed
    // col+1.
    // 
    // Target Complexity: O(1)
    public int cols() {
        return theCols.size();
    }
    
    // Return the fill element with which this matrix was initialized
    // 
    // Target Complexity: O(1)
    public T getFillElem() {
        return fillElem;
    }
    
    // Add an empty row on to the bottom of the matrix.
    // 
    // Target Complexity: O(1) amortized
    public void addRow() {
        // Add a new Head on to the bottom of the matrix, adding this Head is like adding a new row
        // This row number for this Head is the size of theRows because of zero indexing.
        theRows.add(new Head<T>(theRows.size()));
    }
    
    // Add an empty col on right side of the matrix.
    // 
    // Target Complexity: O(1) amortized
    public void addCol() {
        // Add a new Head on right side of the matrix, adding this Head is like adding a new column
        // This row number for this Head is the size of theRows because of zero indexing.
        theCols.add(new Head<T>(theCols.size()));
    }
    
    // Insert an empty row at position i. Later rows are "shifted down"
    // to a higher index.  Importantly, Nodes should not need
    // adjustments; only the Head indices should need alteration.
    //
    // Target Complexity: O(R)
    //   R: number of rows in the matrix
    public void insertRow(int i) {
        if(i > theRows.size() || i < 0) {
            throw new IndexOutOfBoundsException("We don't have that many rows yet");
        }
        // Add an empty row at the end
        theRows.add(null);
        // j = theRows.size() - 2 because the first element we want to shift
        // is one index before the last position.
        for(int j = theRows.size() - 2; j >= i; j--) {
            // get the Head at position j, make temp refers to it.
            // move the temp(Head at position j) to position j + 1(shift down). 
            // The index field of the Head also need to plus 1.
            Head<T> temp = theRows.get(j);
            theRows.set(j + 1, temp); 
            temp.index++;
        }
        // set the Head at position i to a new Head, so it is an empty new row
        theRows.set(i, new Head<T>(i));
    }
    
    // Insert an empty col at position i. Later cols are "shifted right"
    // to a higher index.  Importantly, Nodes should not need
    // adjustments; only the Head indices should need alteration.
    //
    // Target Complexity: O(C)
    //   C: number of cols in the matrix
    public void insertCol(int i) {
        if(i > theCols.size() || i < 0) {
            throw new IndexOutOfBoundsException("We don't have that many columns yet");
        }
        // Add an empty column at the end
        theCols.add(null);
        for(int j = theCols.size() - 2; j >= i; j--) {
            // get the Head at position j, make temp refers to it.
            // move the temp(Head at position j) to position j + 1(shift right). 
            // The index field of the Head also need to plus 1.
            Head<T> temp = theCols.get(j);
            theCols.set(j + 1, temp); 
            temp.index++;
        }
        // set the Head at position i to a new Head, so it is an empty new column
        theCols.set(i, new Head<T>(i));
    }
    
    // Retrieve the element at position (i,j) in the matrix. If the
    // position is out of bounds, throw an IndexOutOfBoundsException
    // with an appropriate message. Otherwise, access the target row or
    // column and walk the list to locate the element. If no node for
    // the element exists, return the fillElem for this
    // matrix. Otherwise return the data found in the target node.
    //
    // Target Complexity: O(E)
    //   E: number of non-fill elements in the matrix
    public T get(int i, int j) {
        if(i >= theRows.size() || i < 0) {
            throw new IndexOutOfBoundsException("We don't have that many rows yet");
        }
        if(j >= theCols.size() || j < 0) {
            throw new IndexOutOfBoundsException("We don't have that many columns yet");
        }
        /*
         * First get the Head of row i,
         * traverse that row to see if there are data Nodes.
         * If there are no Nodes, return fillElment.
         * If there are Nodes, find the column position of the Nodes one by one until it matches with j.
         * If no match, return fillElment.
         */ 
        Head<T> rowHead = theRows.get(i);
        // Get dummy Node, and then look to its right because it is a row
        Node<T> tempNode = rowHead.nodes.right;
        while(tempNode!= null) {
            if(tempNode.colHead.index > j) {
                // We passed postion (i, j), and did not find a node at that position,
                // no need to traverse anymore.
                break;
            }
            if(tempNode.colHead.index == j) {
                // Found the node at postion (i, j), return the data in the node
                return tempNode.data;
            }
            tempNode = tempNode.right;
        }
        // No node at postion (i, j), return fillElem.
        return fillElem;
    }
    
    // Set element at position (i,j) to be x.
    // 
    // If x is the fillElem, throw an IllegalArgumentException with an
    // appropriate message.
    // 
    // If position (i,j) already has an element present, alter that
    // element.
    // 
    // Otherwise, allocate a new node and link it into any existing
    // nodes by traversing the appropriate row and column lists.
    //
    // This method automatically expands the size of the matrix via
    // repeated calls to addRow() and addCol() to ensure that position
    // (i,j) is available. It does not throw any
    // IndexOutOfBoundsExceptions.  If i or j is negative, also throw an
    // IllegalArgumentException. 
    //
    // Target complexity: 
    // O(E) when (i,j) is in bounds
    // O(E+R+C) when (i,j) is out of bounds requiring expansion
    //   E: number of non-fill elements
    //   R: number of rows in the matrix
    //   C: number of cols in the matrix
    public void set(int i, int j, T x) {
        if(i < 0 || j < 0) { 
            throw new IllegalArgumentException("i or j cannot be negative");
        } 
        if(x.equals(this.fillElem)) {
            throw new IllegalArgumentException("To set to fillElement, use setToFill instead .");
        }
        
        // Expand the rows or columns to avoid index out of bound.
        while(i >= theRows.size()) {
            this.addRow();
        }
        while(j >= theCols.size()) {
            this.addCol();
        }
        
        Node<T> newNode = new Node<T>(); // The new dataNode we need if there is no Node at position(i,j)
        Head<T> rowHead = theRows.get(i); 
        Head<T> colHead = theCols.get(j); 
        // Set tempNode to dummy Node of row Head at position i.
        // so that we can traverse the row
        Node<T> tempNode = rowHead.nodes;
        while(tempNode.right != null) {
            if(tempNode.right.colHead.index > j) {
                // tempNode.right has a column index that is greater than j
                // it means we have to insert the newNode to the left of tempNode.right
                // and then point the newNode to tempNode.right
                newNode.right = tempNode.right;
                break;
            }
            if(tempNode.right.colHead.index == j) {
                // Position(i, j) already has a Node,so we don't need the newNode
                // just modify the data of the node at position(i,j), and return.
                tempNode.right.data = x;
                return;
            }
            tempNode = tempNode.right;
        }  
        // When the execution comes to here, it means position(i,j) had no Node before,
        // and is now filled with the newly created Node.
        // so we need to point the tempNode to the newNode.
        tempNode.right = newNode;
      
        // Set tempNode to the dummy Node of column Head at position j.
        // so that we can traverse the column
        tempNode = colHead.nodes;
        while(tempNode.down != null) {
            if(tempNode.down.rowHead.index > i) {
                // tempNode.down has a row index that is greater than i
                // it means our newly inserted Node is above tempNode.down
                // we need to point our newNode to tempNode.down
                newNode.down = tempNode.down;
                break;
            }
            tempNode = tempNode.down;
        }  
        // point the tempNode to the newNode
        tempNode.down = newNode;
        
        // Modify newNode's data, 
        // point newNode to rowHead and colHead
        // a newNode is inserted, increase size by one. 
        newNode.data = x;
        newNode.rowHead = rowHead;
        newNode.colHead = colHead;
        this.size++;  
    }
    
    // Set the element at position (i,j) to be the fill element.
    // Internally this should remove any node at that position by
    // unlinking it from row and column lists.  If no data exists at
    // (i,j), no changes are made to the matrix.  If (i,j) is out of
    // bounds, throw an IndexOutOfBoundsException with an appropriate
    // message.
    //
    // Target Complexity: O(E)
    //   E: number of non-fill elements in the matrix
    public void setToFill(int i, int j) {
        if(i >= theRows.size() || i < 0) {
            throw new IndexOutOfBoundsException("We don't have that many rows yet");
        }
        if(j >= theCols.size() || j < 0) {
            throw new IndexOutOfBoundsException("We don't have that many columns yet");
        }
       
        Head<T> rowHead = theRows.get(i); 
        Head<T> colHead = theCols.get(j); 
        // Set tempNode to dummy Node of row Head at position i.
        // so that we can traverse the row
        Node<T> tempNode = rowHead.nodes;
        while(tempNode.right != null) {
            if(tempNode.right.colHead.index > j) {
                // tempNode.right has a column index that is greater than j
                // it means there is no Node at position(i,j)
                return;
            }
            if(tempNode.right.colHead.index == j) {
                // Position(i, j) has a Node,
                // We unlink it by pointing tempNode to tempNode.right.right,
                // so we bypass the Node tempNode.right
                tempNode.right = tempNode.right.right;
                break;
            }
            tempNode = tempNode.right;
        }  
        // When the execution comes to here, it means position(i,j) has a Node,
        // and we want to unlink it from column list too
        // Set tempNode to the dummy Node of column Head at position j.
        // so that we can traverse the column
        tempNode = colHead.nodes;
        while(tempNode.down != null) {
            if(tempNode.down.rowHead.index == i) {
                // Position(i, j) has a Node,
                // We unlink it by pointing tempNode tempNode.down.down,
                // so we bypass the Node tempNode.down
                tempNode.down = tempNode.down.down;
                // finish unlinking the Node from both row and column,
                // decrease the size by one
                this.size--;
                break;
            }
            tempNode = tempNode.down;
        }  
    }
    
    // Create a display version of the sparse matrix. Each element
    // (including fills) is shown in a grid of elements. Each element
    // will be in a field of width 5 characters with a space after
    // it. Using String.format("%5s ",el) is useful to create the
    // string.  Each row is on its own line.
    //
    // Example:
    // SparseMatrix<Double> x = new SparseMatrix<Double>(5,4, 0.0); 
    // x.set(1,1, 1.0);
    // x.set(2,1, 2.0);
    // x.set(0,3, 3.0);
    // System.out.println(x);
    //   0.0   0.0   0.0   3.0 
    //   0.0   1.0   0.0   0.0 
    //   0.0   2.0   0.0   0.0 
    //   0.0   0.0   0.0   0.0 
    //   0.0   0.0   0.0   0.0 
    //
    // Target Complexity: O(R*C)
    //   R: number of rows in the matrix
    //   C: number of cols in the matrix
    //   E: number of non-fill elements in the matrix
    // Note: repeated calls to get(i,j) will not adhere to this
    // complexity
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < theRows.size(); i++) {
            Head<T> rowHead = theRows.get(i);
            Node<T> tempNode = rowHead.nodes.right;
            int currentPos = 0;
          
            while(tempNode != null) {
                int nodePos = tempNode.colHead.index; 
                while(currentPos < nodePos) {
                    // Until currenet position is at tempNode's column position, 
                    // append fillElement for each position
                    builder.append(String.format("%5s ",this.fillElem));
                    currentPos++;
                }
                // Now at tempNode's position
                // append tempNode's data
                // move ahead one position 
                // move to next node by pointing tempNode to next node
                builder.append(String.format("%5s ",tempNode.data));
                currentPos++;
                tempNode = tempNode.right;
            }
            
            // tempNode is null, if current position is not at the end of the row
            // append fillElements until current position is at the end of the row
            while(currentPos < theCols.size()) {
                builder.append(String.format("%5s ",this.fillElem));
                currentPos++;
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
    // Required but may simply return "".  This method will be called
    // and the string it produces will be reported when tests fail to
    // aid in viewing the internal state of the SparseMatrix for
    // debugging.
    public String debugString() {
        return "";
    }
    
    // Produce a List of all elements as triplets of (i,j,data). The
    // List may be any kind (ArrayList/LinkedList) and in any order.
    //
    // Target Complexity: O(R + E)
    //   R: number of rows in the matrix
    //   E: number of non-fill elements in the matrix
    // Note: repeated calls to get(i,j) will not adhere to this
    // complexity
    public List<Triple<Integer,Integer,T>> allElements() {
        List<Triple<Integer,Integer,T>> elementList = new ArrayList<Triple<Integer,Integer,T>>();
        for(int i = 0; i < theRows.size(); i++) {
            // Get the first Node of the row if there is one.
            Node<T> tempNode = theRows.get(i).nodes.right;
            while(tempNode != null) {
                // Add a new Triple to elementList using Node's information
                elementList.add(new Triple<Integer,Integer,T>(tempNode.rowHead.index, tempNode.colHead.index, tempNode.data));
                tempNode = tempNode.right;
            }
        }
        return elementList;
    }
    
    // Add two sparse matrices of Doubles together in an elementwise
    // fashion and produce another SparseMatrix as the result.  The fill
    // element of the resulting matrix is the sum of the two fill
    // elements from matrices x and y.  If the sum of any two elements
    // in the matrices equals the resulting fill element, that should
    // not occupy a node in the resulting sparse matrix.
    // 
    // Matrices must have the same size (rows,cols) to be added. Throw
    // an IllegalArgumentException with an appropriate message if not.
    //
    // Target Complexity: O(R + C + Ex + Ey)
    //   R: Number of rows in matrices x and y
    //   C: Number of cols in matrices x and y
    //   Ex, Ey: Number of non-fill elements in matrix x and y
    // Memory constraint: O(1)
    //   The memory constraint does not count the size of x, y, or the
    //   result matrix which is returned.
    public static SparseMatrix<Double> addFast(SparseMatrix<Double> x, SparseMatrix<Double> y) {
        /*
         * Creating a new Matrix will require R + C steps.
         * A for loop that loop through rows has R steps
         * A while loop within the for loop to travese data Nodes in matrix x and y
         * in a single row. This is not O(R * (Ex + Ey)) 
         * because the while loop does not execute (Ex + Ey) steps each time for loop 
         * executes. Say if all elements of matrix x and y are in the first row,
         * Staring from second row till the last row, the while loop will always be false 
         * because there are no Nodes in matrix x any y anymore. 
         * The for while nested loops is O(R + Ex + Ey) because total number of times
         * the while loop gets executed in this nested loop will be Ex + Ey.
         * 
         * The for while nested loops for linking columns are O(R + Ex + Ey) as well
         * because we only visit data Nodes in result matrix for linking, 
         * the number of dataNodes in result matrix is less or equal than Ex + Ey,
         * which is the total number of times the while loop gets executed in this nested loop
         * We only visit a Node once when we link it to its down Node, so this 
         * is constant time because linking takes constant steps as well.
         * So this method will be O(R + C + Ex + Ey)
         */
        
        if(x.theRows.size() != y.theRows.size() || x.theCols.size() != y.theCols.size()) {
            throw new IllegalArgumentException("Cannot add two Matrices with different sizes");
        }
        
        SparseMatrix<Double> resultMatrix = 
            new SparseMatrix<Double>(x.theRows.size(), x.theCols.size(), x.getFillElem() + y.getFillElem());
      
        if(x.size == 0 && y.size == 0) {
            // If there are no Nodes in matrix x and y, there will be no Nodes in resultMatrix
            return resultMatrix;
        }
        
        for(int i = 0; i < resultMatrix.theRows.size(); i++) {
            Head<Double> rowHeadX = x.theRows.get(i); // The rowHead of row i in matrix x
            Head<Double> rowHeadY = y.theRows.get(i); // The rowHead of row i in matrix y
            Head<Double> rowHeadResult = resultMatrix.theRows.get(i); // The rowHead of row i in resultMatrix
        
            Node<Double> rowHeadXTempNode = rowHeadX.nodes.right; // tempNode to traverse row i in matrix x
            Node<Double> rowHeadYTempNode = rowHeadY.nodes.right; // tempNode to traverse row i in matrix y
            Node<Double> lastNodeOfRowInResult = rowHeadResult.nodes; // This field point to the last Node of row i, in resultMatrix
            
            while(rowHeadXTempNode != null || rowHeadYTempNode != null) {
                // While there is a dataNode in either matrix x or matrix y
                // We are going to add a new dataNode to result matrix
                // so result matrix will increase size by one
                Node<Double> newNode = new Node<Double>();
                resultMatrix.size++;
                if(rowHeadXTempNode == null) {
                    // tempNode of row i in matrix x is null, we are done traversing row i in matrix x
                    // tempNode of row i in matrix y is not null
                    // we need to add this tempNode's data to matrix x's fillElment,
                    // the result is the data of newNode in resultMatrix
                    // The position of newNode in resultMatrix is the same position of tempNode in matrix y
                    // point tempNode in matrix y to its right
                    newNode.data = rowHeadYTempNode.data + x.getFillElem();
                    newNode.rowHead = resultMatrix.theRows.get(rowHeadYTempNode.rowHead.index);
                    newNode.colHead = resultMatrix.theCols.get(rowHeadYTempNode.colHead.index);
                    rowHeadYTempNode = rowHeadYTempNode.right;
                } else if(rowHeadYTempNode == null) {
                    // tempNode of row i in matrix y is null, we are done traversing row i in matrix y
                    // tempNode of row i in matrix x is not null
                    // we need to add this tempNode's data to matrix y's fillElment,
                    // the result is the data of newNode in resultMatrix
                    // The position of newNode in resultMatrix is the same position of tempNode in matrix x
                    // point tempNode in matrix x to its right
                    newNode.data = rowHeadXTempNode.data + y.getFillElem();
                    newNode.rowHead = resultMatrix.theRows.get(rowHeadXTempNode.rowHead.index);
                    newNode.colHead = resultMatrix.theCols.get(rowHeadXTempNode.colHead.index);
                    rowHeadXTempNode = rowHeadXTempNode.right;
                } else {
                    // We are not done traversing row i in both matrix x and y
                    if(rowHeadXTempNode.colHead.index > rowHeadYTempNode.colHead.index) {
                        // tempNode in matrix x is in a later column position than tempNode in matrix y
                        // so we add the data of tempNode in matrix y to matrix x's fillElment, 
                        // the result is the data of newNode in resultMatrix
                        // The position of newNode in resultMatrix is the same position of tempNode in matrix y
                        // point tempNode in matrix y to its right
                        newNode.data = rowHeadYTempNode.data + x.getFillElem();
                        newNode.rowHead = resultMatrix.theRows.get(rowHeadYTempNode.rowHead.index);
                        newNode.colHead = resultMatrix.theCols.get(rowHeadYTempNode.colHead.index);
                        rowHeadYTempNode = rowHeadYTempNode.right;
                    } else if(rowHeadXTempNode.colHead.index < rowHeadYTempNode.colHead.index) {
                        // tempNode in matrix y is in a later column position than tempNode in matrix x
                        // so we add the data of tempNode in matrix x to matrix y's fillElment, 
                        // the result is the data of newNode in resultMatrix
                        // The position of newNode in resultMatrix is the same position of tempNode in matrix x
                        // point tempNode in matrix x to its right
                        newNode.data = rowHeadXTempNode.data + y.getFillElem();
                        newNode.rowHead = resultMatrix.theRows.get(rowHeadXTempNode.rowHead.index);
                        newNode.colHead = resultMatrix.theCols.get(rowHeadXTempNode.colHead.index);
                        rowHeadXTempNode = rowHeadXTempNode.right;
                    } else {
                        // tempNode in matrix y is in the same column position as tempNode in matrix x
                        // so we add the data of tempNode in matrix x to the data of tempNode in matrix y 
                        Double resultData = rowHeadYTempNode.data + rowHeadXTempNode.data;
                        if(!resultData.equals(resultMatrix.getFillElem())) {
                            // if the result is not the fillElement of resultMatrix
                            // the result is the data of newNode in resultMatrix
                            // The position of newNode in resultMatrix is the same position of tempNode in matrix x and y
                            // point tempNodes in matrix x and y to their right
                            newNode.data = resultData;
                            newNode.rowHead = resultMatrix.theRows.get(rowHeadXTempNode.rowHead.index);
                            newNode.colHead = resultMatrix.theCols.get(rowHeadXTempNode.colHead.index);
                            rowHeadXTempNode = rowHeadXTempNode.right;
                            rowHeadYTempNode = rowHeadYTempNode.right;
                        } else {
                            // the result is the fillElement of resultMatrix
                            // we are not adding the newNode to the resultMatrix
                            // since I increased the size of resultMatrix at the begining
                            // assuming the newNode will not have fillElement as its data
                            // I need to cancel that increase by decreasing the size by one
                            // point tempNodes in matrix x and y to their right
                            // continue back to while loop 
                            // because we have not added a newNode to resultMatrix
                            resultMatrix.size--;
                            rowHeadXTempNode = rowHeadXTempNode.right;
                            rowHeadYTempNode = rowHeadYTempNode.right;
                            continue;
                        }
                    }
                }
                // If we have successfully added a newNode to resultMatrix
                // we point the lastNode of row i in resultMatrix to the newNode
                // after that, newNode becomes the lastNode of row i in resultMatrix
                lastNodeOfRowInResult.right = newNode;
                lastNodeOfRowInResult = newNode;
            }
        }
        
        /*
         * Link the columns of resultMatrix  
         * We start from bottom row to top row,
         * This way will be more efficent because we only need two steps to link 
         * a Node to its down Node
         * For each Node, we first get dummyNode of its colHead
         * we then point the tempNode to what the dummyNode is pointing,
         * then point dummyNode to the tempNode
         * Because dummyNode is pointing to the tempNode, 
         * we can easily point the Node above tempNode to tempNode
         */
        for(int i = resultMatrix.theRows.size() - 1; i >= 0; i--) {
            // We start from bottom row to top row
            // This way we don't have to start from top row each time
            // when link a Node to its down Node
            Head<Double> rowHead = resultMatrix.theRows.get(i); 
            Node<Double> tempNode = rowHead.nodes.right; // tempNode to traverse the Nodes in row i
            while(tempNode != null) {
                // get dummyNode of tempNode's colHead 
                Node<Double> dummyNode = tempNode.colHead.nodes;
                // point tempNode to what the dummyNode is pointing
                // then point dummyNode to tempNode
                // point tempNode to its right Node
                tempNode.down = dummyNode.down;
                dummyNode.down = tempNode;
                tempNode = tempNode.right;
            }
        }
        return resultMatrix;
    }
    
    public static void main(String args[]) {
        
    }
    
}