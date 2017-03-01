// Provided class to convey triples of objects. For compareTo(other)
// presumes that each of the constituent objects are also comparable.
public class Triple<A,B,C> implements Comparable<Triple<A,B,C>>
{
  public final A a;
  public final B b;
  public final C c;
  public Triple(A a, B b, C c){
    this.a = a; this.b = b; this.c = c;
  }
  public boolean equals(Object o){
    if(o==null || !(o instanceof Triple)){
      return false;
    }
    Triple that = (Triple) o;
    return
      this.a.equals(that.a) && 
      this.b.equals(that.b) && 
      this.c.equals(that.c);
  }
  @SuppressWarnings("unchecked")
  public int compareTo(Triple<A,B,C> that){
    int diff;
    diff = ((Comparable<A>) this.a).compareTo(that.a);
    if(diff!=0){ return diff; }
    diff = ((Comparable<B>) this.b).compareTo(that.b);
    if(diff!=0){ return diff; }
    diff = ((Comparable<C>) this.c).compareTo(that.c);
    return diff;
  }
    

  public String toString(){
    return String.format("(%s, %s, %s)", this.a,this.b,this.c);
  }      
}
