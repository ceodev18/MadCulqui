# MadCulqui
Esta implementación se realizó para un proyecto privado, pero siento que quedo muy bien implementado que decidi compartirlo para la comunidad.

## Configuración
Agrega en el build.gradle del proyecto
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  
  Y agrega la dependencia en el módulo que lo utilizará (build.gradle)
  ```Java
  dependencies {
	        implementation 'com.github.maddog05:MadCulqui:Tag'
	}
 ```
## Cómo usarlo
Tienes las siguientes implementaciones para:
Java con patrón Builder
```Java
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
```
 Y Kotlin en 2 formas, como java 8 o patrón Builder
```Kotlin
MadCulqui(TOKEN).apply {
            card = Card().apply {
                number = ""
                expirationMonth = 1
                expirationYear = 2020
                cvv = "123"
                email = "a@a.com"
            }
        }.generateToken(object : OnGenerateTokenListener {
            override fun onSuccess(token: String) {

            }

            override fun onError(errorMessage: String) {

            }
        })
        
        MadCulqui.with(TOKEN)
            .setCard(
                Card.Builder()
                    .number("")
                    .expirationMonth(1)
                    .expirationYear(2020)
                    .cvv("123")
                    .email("a@a.com")
                    .build()
            ).generateToken(object : OnGenerateTokenListener {
                override fun onSuccess(token: String) {

                }

                override fun onError(errorMessage: String) {

                }
            })
```

## Incidencias y sugerencias
Solo abre issues sobre incidencias con la librería, EL API DE CULQUI ES EXTERNO y errores sobre del api favor de reportarlo en su respectivo proyecto [Culqui Android Demo](https://github.com/culqi/culqi-android) o revisar la documentación del [API](https://www.culqi.com/docs/#/) 


