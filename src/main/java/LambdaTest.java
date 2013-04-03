/**
 * Created with IntelliJ IDEA.
 * User: ae589
 * Date: 02/03/2013
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public class LambdaTest {

    @FunctionalInterface
    interface Function1<T1,T2> {
        public T1 apply(T2 t);
    }

    public static void main(String[] args){

        tiffany((String x) -> x+"amigo");
        tiffany((String x) -> x+"amigos");
        Function1<String,String> t = (String x) -> x+"honcho";
        tiffany(LambdaTest::echo);
    }
    private static String echo(String in){
        return in;
    }
    private static void tiffany(Function1<String,String> t){
        System.out.println(t.apply("Yo "));
    }
}
