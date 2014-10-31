package org.onelib.util.calculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chunliangh on 14-9-9.
 */
public class Calculator {
    /**
     * 已知集合A,给定值m,对于任意集合b1属于A+，求A+中的元素b,满足b中所有元素的乘积bx<=b1中所有元素的乘积，且bx>=m
     * @param src 给定的集合A
     * @param max 给定的值m
     * @return
     */
    public static Object[] lowerLimit(List<Float> src, float max){
        Collections.sort(src);
        int len = src.size();
        if (len==0) return null;
        int[] boundaries = calculateBoundaries(src, len, max);
        if (boundaries[0]>0){
            float sum=0;
            Object[] pre = null;
            for (int i=boundaries[0];i<boundaries[1]+1;i++){
                Object[] rsN = produceN(i, len, src, max);
                if (i==boundaries[0]){
                    sum = (Float)rsN[0];
                    pre = rsN;
                } else {
                    if (sum>(Float)rsN[0]){
                        sum = (Float)rsN[0];
                        pre = rsN;
                    }
                }
            }
            return pre;
        }
        return null;
    }
    //获取集合的元素个数边界
    private static int[] calculateBoundaries(List<Float> src, int len, float max){
        int nMax = 0, nMin = 0;
        float sum = 1;
        //求最小边界
        for (int i=len-1;i>=0;i--){
            sum*=src.get(i);
            if (sum>=max){
                nMin = len-i;
                break;
            }
        }
        if (nMin==0) return new int[]{0,0};
        //求最大边界
        if(src.get(0)<=1&&src.get(len-1)<=1){
            sum = 1;
            for (int i=len-1;i>=0;i--){
                sum*=src.get(i);
                if (sum>=max){
                    nMax = len-i;
                } else {
                    break;
                }
            }
        } else if (src.get(0)>=1&&src.get(len-1)>=1){
            sum = 1;
            for (int i=0;i<len;i++){
                sum*=src.get(i);
                if (sum>=max){
                    nMax = i+1;
                    break;
                }
            }
        } else {
            sum = 1;
            int nMax_1 = 0, nMax_2 = 0;
            for (int i=len-1;i>=0;i--){
                sum*=src.get(i);
                if (sum>=max){
                    nMax_1 = len-i;
                }
            }
            sum = 1;
            for (int i=0;i<len;i++){
                sum*=src.get(i);
                if (sum>=max){
                    nMax_2 = i+1;
                }
            }
            if (nMax_1>0 && nMax_2>0){
                nMax = nMax_1<nMax_2 ? nMax_1:nMax_2;
            } else {
                nMax = nMax_1==0 ? nMax_2:nMax_1;
            }
        }
        return new int[]{nMin,nMax};
    }

    /**
     * 确定数组中的n个数组成的集合A及这n个数的乘积pn，使得pn大于max且对于任意n个数的乘积pn_,都有pn<=pn_
     * @param n
     * @param srcLen 给定数组的长度 满足条件：srcLen>=n
     * @param src 给定的数组 满足条件：数组元素是正数且数组是正序排列的，存在n个数组成的集合A及这n个数的乘积pn，使得pn大于max
     * @param max
     * @return [pn,A]
     */
    public static Object[] produceN(int n, int srcLen, List<Float> src, float max){
        assert srcLen>=n;
        float sum = 1;
        if (srcLen==n){//如果长度等于n直接返回
            for (Float f:src) sum*=f;
            return new Object[]{sum,src};
        }
        List<Float> content = new ArrayList<Float>(n);
        int preIndex = srcLen-1, ln = n;//待确定的数目
        for (int i=srcLen-2;i>=ln-1;i--){
            if (ln==0) break;//如果待确定数目为0结束循环
            float s = 1;//当前所有元素的乘积
            for (int j=0;j<ln;j++) s *= src.get(i-j);
            if (s*sum<max) {
                float preValue = src.get(preIndex);
                sum *= preValue;
                content.add(preValue);
                preIndex = i; ln--;
            } else {
                if (ln==i+1) {//如果待确定数目为剩余数目，则剩余数据为确定数据,否则继续向前找
                    for (int k=i;k>=0;k--){
                        float preValue = src.get(k);
                        sum*=preValue;
                        content.add(preValue);
                    } break;
                } else { preIndex = i; }
            }
        }
        return new Object[]{sum,content};
    }
    public static void main(String[] args){
        List<Float> src = new ArrayList<Float>();
        src.add(1f);
        src.add(2f);
        src.add(3f);
        src.add(4f);
        src.add(5f);
        Object[] arr = produceN(2,5,src,100);
        System.out.println(arr[0]);
        System.out.println(arr[1]);
    }
}
