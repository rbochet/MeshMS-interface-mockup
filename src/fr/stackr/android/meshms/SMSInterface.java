package fr.stackr.android.meshms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SMSInterface extends Activity implements OnClickListener {
	private static final int CONTACT_PICKER_RESULT = 1001;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button pick_contact = (Button) findViewById(R.id.meshms_pick_contact);
		pick_contact.setOnClickListener(this);

		final Button send = (Button) findViewById(R.id.meshms_send);
		send.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.meshms_pick_contact))) {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
					Contacts.CONTENT_URI);
			startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
		} else if (v.equals(findViewById(R.id.meshms_send))) {
			sendMeshMS();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				final EditText phoneInput = (EditText) findViewById(R.id.meshms_to);
				Cursor cursor = null;
				String phoneNumber = "";
				List<String> allNumbers = new ArrayList<String>();
				int phoneIdx = 0;
				try {
					Uri result = data.getData();
					String id = result.getLastPathSegment();
					cursor = getContentResolver().query(Phone.CONTENT_URI,
							null, Phone.CONTACT_ID + "=?", new String[] { id },
							null);
					phoneIdx = cursor.getColumnIndex(Phone.DATA);
					if (cursor.moveToFirst()) {
						while (cursor.isAfterLast() == false) {
							phoneNumber = cursor.getString(phoneIdx);
							allNumbers.add(phoneNumber);
							cursor.moveToNext();
						}
					} else {
					}
				} catch (Exception e) {
				} finally {
					if (cursor != null) {
						cursor.close();
					}

					final CharSequence[] items = allNumbers
							.toArray(new String[allNumbers.size()]);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SMSInterface.this);
					builder.setTitle(getString(R.string.meshms_choose_number));
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									String selectedNumber = items[item]
											.toString();
									selectedNumber = selectedNumber.replace(
											"-", "");
									phoneInput.setText(selectedNumber);
								}
							});
					AlertDialog alert = builder.create();
					if (allNumbers.size() > 1) {
						alert.show();
					} else {
						String selectedNumber = phoneNumber.toString();
						selectedNumber = selectedNumber.replace("-", "");
						phoneInput.setText(selectedNumber);
					}
				}
				break;
			}
		} else {
		}
	}

	private void sendMeshMS() {
		// TODO Auto-generated method stub
	}
}