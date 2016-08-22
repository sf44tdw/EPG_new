/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epgtools.reverselookupmapfactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.Range;

/**
 * 1つ以上の数値を受け取って、重複のない数値セット(変更不可)を作る。
 *
 * @author normal
 * @param <T> セットの数値型
 */
public final class DeduplicatdeNumberSetFactory<T extends Number> {

    public DeduplicatdeNumberSetFactory() {
    }

    /**
     *
     * @param numberRange セットに加える数値の範囲。
     * @param number 数値 null不可。
     * @param numbers 数値のリスト。省略可。ただしnullを含んではならない。
     * @return 重複のない数値セット(変更不可)
     * @throws NullPointerException 引数のどこかにnullがあった場合。
     * @throws IllegalArgumentException 引数の値がnumberrangeで設定された範囲外だった場合。
     */
    public Set<T> makeSet(Range<T> numberRange, T number, T... numbers) throws NullPointerException, IllegalArgumentException {

        List<T> t = new ArrayList<>();
        t.add(number);

        if (numbers != null) {
            t.addAll(Arrays.asList(numbers));
        }
        for (T num : t) {
            if (num == null) {
                throw new IllegalArgumentException("引数リスト内にnullが混じっています。");
            }
            if (!numberRange.contains(num)) {
                MessageFormat msg = new MessageFormat("範囲外の値が含まれています。範囲外になった値={0}");
                Object[] parameters = {num};
                throw new IllegalArgumentException(msg.format(parameters));
            }
        }
        Set<T> numberSet_t = Collections.synchronizedSet(new HashSet<>());
        numberSet_t.addAll(t);
        return Collections.unmodifiableSet(numberSet_t);
    }

}
