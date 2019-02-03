package com.maddog05.demomadculqui.implementation;

import com.maddog05.madculqui.MadCulqui;
import com.maddog05.madculqui.callback.OnGenerateTokenListener;
import com.maddog05.madculqui.entity.Card;
import org.jetbrains.annotations.NotNull;

public class JavaImpl {
    public static final String TOKEN = "";

    public static void generateToken() {
        MadCulqui.with(TOKEN)
                .setCard(new Card.Builder()
                        .number("")
                        .expirationMonth(1)
                        .expirationYear(2020)
                        .cvv("123")
                        .email("a@a.com")
                        .build())
                .generateToken(new OnGenerateTokenListener() {
                    @Override
                    public void onSuccess(@NotNull String token) {

                    }

                    @Override
                    public void onError(@NotNull String errorMessage) {

                    }
                });
    }
}
