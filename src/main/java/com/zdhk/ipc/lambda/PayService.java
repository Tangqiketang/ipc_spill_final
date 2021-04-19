package com.zdhk.ipc.lambda;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2021-03-26 10:41
 */
public class PayService {

    public static void pay(PayMoneyFunction payMoneyFunction){
        payMoneyFunction.pay();
    }

    public static void main(String[] args) {
        //实现payMoneyFunction的抽象方法

        pay(()-> {
            System.out.println("我想调用支付宝service");
            return 0.5d;
        });

        pay(()-> {
            System.out.println("我想调用微信service");
            return 0.6d;
        });


    }

}
