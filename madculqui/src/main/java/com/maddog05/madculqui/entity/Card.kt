package com.maddog05.madculqui.entity

open class Card {
    var number = ""
    var expirationMonth = 0
    var expirationYear = 0
    var cvv = ""
    var email = ""

    class Builder {
        private var bNumber = ""
        private var bExpirationMonth = 0
        private var bExpirationYear = 0
        private var bCvv = ""
        private var bEmail = ""

        fun number(number: String): Builder {
            this.bNumber = number
            return this
        }

        fun expirationMonth(expirationMonth: Int): Builder {
            this.bExpirationMonth = expirationMonth
            return this
        }

        fun expirationYear(expirationYear: Int): Builder {
            this.bExpirationYear = expirationYear
            return this
        }

        fun cvv(cvv: String): Builder {
            this.bCvv = cvv
            return this
        }

        fun email(email: String): Builder {
            this.bEmail = email
            return this
        }

        fun build(): Card {
            return Card().apply {
                number = bNumber
                expirationMonth = bExpirationMonth
                expirationYear = bExpirationYear
                cvv = bCvv
                email = bEmail
            }
        }
    }
}