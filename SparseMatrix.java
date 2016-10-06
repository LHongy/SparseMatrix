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
        
        // Constructor for a new Node 
        public Node() {
            
        }
        
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
        for(int j = theRows.size() - 1; j >= i; j--) {
            // get the Head at position j, make temp refers to it.
            // move the temp(Head at position j) to position j + 1(shift down). 
            // The index field of the Head also need to plus 1.
            Head<T> temp = theRows.get(j);
            theRows.set(j + 1, temp); 
            temp.index++;
        }
        // Insert A Head at position i, this is equivalent to insert an empty row
        theRows.set(i, new Head<T>(i));
    }
    
    // Insert an empty col at position i. Later cols are "shifted right"
    // to a higher index.  Importantly, Nodes should not need
    // adjustments; only the Head indices should need alteration.
    //
    // Target Complexity: O(C)
    //   C: number of cols in the matrix
    public void insertCol(int i) {
        for(int j = theCols.size() - 1; j >= i; j--) {
            // get the Head at position j, make temp refers to it.
            // move the temp(Head at position j) to position j + 1(shift right). 
            // The index field of the Head also need to plus 1.
            Head<T> temp = theCols.get(j);
            theCols.set(j + 1, temp); 
            temp.index++;
        }
        // Insert A Head at position i, this is equivalent to insert an empty column
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
            throw new IndexOutOfBoundsException();
        }
        if(j >= theCols.size() || j < 0) {
            throw new IndexOutOfBoundsException();
        }
        /*
         * First get the Head at row i,
         * traverse that row to see if there are data Nodes.
         * If there are no Nodes, return fillElment.
         * If there are Nodes, find the column position of the Nodes one by one until it matches with j.
         * If no match, return fillElment.
         */ 
        Head<T> temp = theRows.get(i);
        // Get dummy Node, and then look to its right because it is a row
        Node<T> tempNode = temp.nodes.right;
        while(tempNode != null) {
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
        return null;
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
        return null;
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
        // Look at the first matrix first, for each row, 
        // First matrix data node only add to fillElement node in second matrix,
        // if second matrix has data node at that position as well,
        // Ignore, so it means don't add the two nodes in two matrix.
        // But Second matrix data node add to both fillElement and data node in first matrix.
        // Might be wrong, it is more complex than this.
        return null;
    }
    
    public static void main(String args[]) {
        
    }
    
}