package com.example.locateme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class FirstMainActivity extends Activity implements OnClickListener{

	Session session = null;
	ProgressDialog pdialog = null;
	Context context = null;
	EditText reciep;
    String emailText="If your mobile is lost or stolen, you can send this code in a text message to the mobile and lost after one second you will receive its location.\n" +
            "\n" +
            "If you are an administrator and you want to detect the location of your carrier sales agents or distributors with our LocateMe everything is quick and easy, just install on their mobile LocateMe.\n" +
            "\n" +
            "If you have children and want to know its locations just install on their mobile LocateMe.";

    static String rec, subject=" Welcome to LocateMe ! ";
	static String code;

    public static String getCode(){

          return code;
    }

    public static String getEmail(){

        return rec;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);
        
        context = this;
        
        Button login = (Button) findViewById(R.id.btn_submit);
        reciep = (EditText) findViewById(R.id.et_to);

        
        login.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		rec = reciep.getText().toString();
        if (rec.equals("")){
            Toast.makeText(FirstMainActivity.this, "Enter your Email ", Toast.LENGTH_LONG)
                    .show();

        }
        else {
            // Generate the encrypted code
            MyStringRandomGen msr = new MyStringRandomGen();
            code = msr.generateRandomString();



            // Prepare Connection to Send mail
            toSend();

            //go to home
            startActivity(new Intent(this, MainActivity.class));


        }
	}



	class RetreiveFeedTask extends AsyncTask<String, Void, String> {


		@Override
		protected String doInBackground(String... params) {
			
			try{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("applocateme@gmail.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
				message.setSubject(subject);
				message.setContent("Thank you for downloading LocateMe for your mobile device." +
                                    " You made the right choice!\n This is your secret code : "+code+".\n"+emailText,
                                    "text/html; charset=utf-8");

				Transport.send(message);
			} catch(MessagingException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pdialog.dismiss();
			Toast.makeText(getApplicationContext(), " Visit your inbox to discover the secret code! ", Toast.LENGTH_LONG).show();

		}
	}






    public void toSend(){


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("applocateme@gmail.com", "applocateme2015");
            }
        });

        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }
}
