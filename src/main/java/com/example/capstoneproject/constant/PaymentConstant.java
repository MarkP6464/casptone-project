package com.example.capstoneproject.constant;

import org.springframework.beans.factory.annotation.Value;

public class PaymentConstant {


    @Value("${quota.ratio}")
    public static Long quotaRatio;

    @Value("${vip.AMonthRatio}")
    public static Long vipAMonthRatio;

    @Value("${vip.AYearRatio}")
    public static Long vipAYearRatio;
}
