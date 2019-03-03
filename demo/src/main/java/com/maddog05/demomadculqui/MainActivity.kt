package com.maddog05.demomadculqui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.maddog05.madculqui.MadCulqui
import com.maddog05.madculqui.callback.OnGenerateTokenListener
import com.maddog05.madculqui.callback.OnPayTransactionListener
import com.maddog05.madculqui.entity.Card
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        btn_action_pay.setOnClickListener { actionPay() }
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.loading_simulate_sale))
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        //setupDemoData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_main_project_site) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/maddog05/MadCulqui"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(Intent.createChooser(intent, getString(R.string.action_choose_app)))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDemoData() {
        et_input_key_public.setText("pk_test_uDB4kd1yrt3BUcnT")
        et_input_key_secret.setText("sk_test_qUkDzQlvMJNIHo1A")
        et_input_email.setText("andree@testing.com")
        et_input_card.setText("4111111111111111")
        et_input_month.setText("9")
        et_input_year.setText("2020")
        et_input_cvv.setText("123")
        et_input_amount.setText("12.50")
        et_input_currency.setText("PEN")
    }

    private fun actionPay() {
        val keyPublic = et_input_key_public.text.toString()
        val keySecret = et_input_key_secret.text.toString()
        val email = et_input_email.text.toString()
        val card = et_input_card.text.toString()
        val month = et_input_month.text.toString().toInt()
        val year = et_input_year.text.toString().toInt()
        val cvv = et_input_cvv.text.toString()
        val amount = et_input_amount.text.toString().toDouble()
        val currency = et_input_currency.text.toString().toUpperCase()
        if (keyPublic.trim().isEmpty() || keySecret.trim().isEmpty())
            showToast("API keys are empty")
        else {
            progressDialog.show()
            debugLog("Generating token from card")
            MadCulqui.with(keyPublic, keySecret)
                .generateTokenRequest()
                .setCard(
                    Card.Builder()
                        .number(card)
                        .expirationMonth(month)
                        .expirationYear(year)
                        .cvv(cvv)
                        .email(email)
                        .build()
                )
                .execute(object : OnGenerateTokenListener {
                    override fun onSuccess(token: String) {
                        debugLog("generateToken: success")
                        payTransaction(token, amount, currency)
                    }

                    override fun onError(errorMessage: String) {
                        if (progressDialog.isShowing)
                            progressDialog.dismiss()
                        errorLog("generateToken: $errorMessage")
                    }
                })
        }
    }

    private fun payTransaction(token: String, amount: Double, currency: String) {
        val keyPublic = et_input_key_public.text.toString()
        val keySecret = et_input_key_secret.text.toString()
        val email = et_input_email.text.toString()
        debugLog("payTransaction with token $token")
        MadCulqui.with(keyPublic, keySecret)
            .payRequest()
            .setAmount(amount)
            .setCurrencyCode(currency)
            .setEmail(email)
            .setSourceId(token)
            .execute(object : OnPayTransactionListener {
                override fun onSuccess(transactionId: String) {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                    debugLog("payTransaction: success: id is $transactionId")
                }

                override fun onError(errorMessage: String) {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                    errorLog("payTransaction: $errorMessage")
                }
            })
    }

    private fun debugLog(text: String) {
        showToast(text)
        Log.d("#Main", text)
    }

    private fun errorLog(text: String) {
        showToast(text)
        Log.e("#Main", text)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
