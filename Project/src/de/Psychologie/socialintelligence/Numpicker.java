package de.Psychologie.socialintelligence;


import net.simonvt.numberpicker.NumberPicker;
import android.os.Bundle;
import android.app.Activity;

public class Numpicker extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_numpicker);
		
		NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(20);
        np.setMinValue(0);
        np.setFocusable(true);
        np.setFocusableInTouchMode(true);
	}
}
