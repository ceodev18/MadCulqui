package com.maddog05.demomadculqui.implementation;

import com.maddog05.madculqui.MadCulqui;
import com.maddog05.madculqui.callback.OnGenerateTokenListener;
import com.maddog05.madculqui.callback.OnPayTransactionListener;
import com.maddog05.madculqui.entity.Card;
import com.maddog05.madculqui.logic.PayRequest;
import org.jetbrains.annotations.NotNull;

public class JavaImpl {
    public static final String PUBLIC_KEY = "";
    public static final String SECRET_KEY = "";

    public static void generateToken() {
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
                .generateTokenRequest()
                .setCard(new Card.Builder()
                        .number("")
                        .expirationMonth(1)
                        .expirationYear(2020)
                        .cvv("123")
                        .email("a@a.com")
                        .build())
                .execute(new OnGenerateTokenListener() {
                    @Override
                    public void onError(@NotNull String errorMessage) {

                    }

                    @Override
                    public void onSuccess(@NotNull String token) {

                    }
                });
    }

    public static void payTransaction() {
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
                .payRequest()
                .setAmount(12.50)
                .setCurrencyCode(PayRequest.CURRENCY_PEN)//ISO 4217
                .setEmail("a@a.com")
                .setSourceId("myToken")
                .execute(new OnPayTransactionListener() {
                    @Override
                    public void onSuccess(@NotNull String transactionId) {

                    }

                    @Override
                    public void onError(@NotNull String errorMessage) {

                    }
                });
    }
}
