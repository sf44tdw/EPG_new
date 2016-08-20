/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtool;

/**
 *
 * @author normal
 */
public final class TestUtil {

    private TestUtil() {
    }

    /**
     * このメソッドを呼び出したメソッドの名前を返す。
     * @return メソッド名
     */
    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * イテレータが使用できるオブジェクトの中身をダンプする。
     * @param target イテレータが使用できるオブジェクト。
     * @return コレクションの中身。
     */
    public static String dumpCollection(Iterable<?> target) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass());
        sb.append("{");
        for (Object s : target) {
            Class c = s.getClass();
            sb.append(c);
            sb.append("=");
            if (c == Iterable.class) {
                dumpCollection((Iterable<?>) s);
            } else {
                sb.append(s);
            }
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

}
