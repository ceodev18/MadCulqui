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
	        implementation 'com.github.maddog05:MadCulqui:1.1.1'
	}
 ```
## Cómo usarlo
Para mas detalle (se irá actualizando) puedes revisar el [Wiki del proyecto](https://github.com/maddog05/MadCulqui/wiki).
Tienes las siguientes implementaciones para:
Java
```Java
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
```
 Y Kotlin
```Kotlin
companion object {
        const val PUBLIC_KEY = ""
        const val SECRET_KEY = ""
    }

    fun generateToken() {
        MadCulqui.with(PUBLIC_KEY, SECRET_KEY)
            .generateTokenRequest()
            .setCard(
                Card.Builder()
                    .number("")
                    .expirationMonth(1)
                    .expirationYear(2020)
                    .cvv("123")
                    .email("a@a.com")
                    .build()
            )
            .execute(object : OnGenerateTokenListener {
                override fun onSuccess(token: String) {

                }

                override fun onError(errorMessage: String) {

                }
            })
    }
```

## Incidencias y sugerencias
Solo abre issues sobre incidencias con la librería, EL API DE CULQUI ES EXTERNO y errores sobre del api favor de reportarlo en su respectivo proyecto [Culqui Android Demo](https://github.com/culqi/culqi-android) o revisar la documentación del [API](https://www.culqi.com/docs/#/) 


