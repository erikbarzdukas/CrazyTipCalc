package com.example.crazytipcalc.app;

import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class CrazyTipCalc extends ActionBarActivity {

    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;
    private double tipAmount;
    private double finalBill;

    EditText billBeforeTipET;
    EditText tipAmountET;
    EditText finalBillET;

    SeekBar tipSeekBar;

    private int[] checklistValues = new int[12];

    CheckBox friendlyCheckBox;
    CheckBox specialsCheckBox;
    CheckBox opinionCheckBox;

    RadioGroup availableRadioGroup;
    RadioButton availableBadRadio;
    RadioButton availableOKRadio;
    RadioButton availableGoodRadio;

    Spinner problemsSpinner;

    Button startChronometerButton;
    Button pauseChronometerButton;
    Button resetChronometerButton;

    Chronometer timeWaitingChronometer;

    long secondsYouWaited = 0;

    TextView timeWaitingTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_tip_calc);

        if(savedInstanceState == null) {

            billBeforeTip = 0.0;
            tipAmount = 0.15;
            finalBill = 0.0;
        } else {

            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        }

        billBeforeTipET = (EditText)findViewById(R.id.billEditText);
        tipAmountET = (EditText)findViewById(R.id.tipEditText);
        finalBillET = (EditText)findViewById(R.id.finalBillEditText);

        tipSeekBar = (SeekBar)findViewById(R.id.changeTipSeekBar);

        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);
        billBeforeTipET.addTextChangedListener(billBeforeTipListener);

        friendlyCheckBox = (CheckBox)findViewById(R.id.friendlyCheckBox);
        specialsCheckBox = (CheckBox)findViewById(R.id.specialsCheckBox);
        opinionCheckBox = (CheckBox)findViewById(R.id.opinionCheckBox);

        setUpIntroCheckBoxes();

        availableRadioGroup = (RadioGroup)findViewById(R.id.availableRadioGroup);
        availableBadRadio = (RadioButton)findViewById(R.id.availableBadRadioButton);
        availableOKRadio = (RadioButton)findViewById(R.id.availableOKRadioButton);
        availableGoodRadio = (RadioButton)findViewById(R.id.availableGoodRadioButton);

        addChangeListenerToRadios();

        problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);

        addItemSelectedListenerToSpinner();

        startChronometerButton = (Button)findViewById(R.id.startChronometerButton);
        pauseChronometerButton = (Button)findViewById(R.id.pauseChronometerButton);
        resetChronometerButton = (Button)findViewById(R.id.resetChronometerButton);

        setButtonOnClickListener();

        timeWaitingChronometer = (Chronometer)findViewById(R.id.timeWaitingChronometer);

        timeWaitingTextView = (TextView)findViewById(R.id.timeWaitingTextView);
    }

    private TextWatcher billBeforeTipListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                billBeforeTip = Double.parseDouble(s.toString());

            } catch(NumberFormatException e) {
                billBeforeTip = 0.0;

            }

            updateTipAndFinalBill();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateTipAndFinalBill() {

        double tipAmount;
        tipAmount = Double.parseDouble(tipAmountET.getText().toString());

        double finalBill = billBeforeTip + (billBeforeTip * tipAmount);

        finalBillET.setText(String.format("%.02f", finalBill));
    }

    protected SeekBar.OnSeekBarChangeListener tipSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            tipAmount = (tipSeekBar.getProgress()) * 0.01;
            tipAmountET.setText(String.format("%.02f", tipAmount));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void setUpIntroCheckBoxes() {

        friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checklistValues[0] = (friendlyCheckBox.isChecked()) ? 4 : 0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }
        });

        specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checklistValues[1] = (specialsCheckBox.isChecked()) ? 1 : 0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }
        });

        opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checklistValues[2] = (opinionCheckBox.isChecked()) ? 2 : 0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }
        });
    }

    private void addChangeListenerToRadios() {

        availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                checklistValues[3] = ((availableBadRadio.isChecked()) ? -2 : 0);
                checklistValues[4] = ((availableOKRadio.isChecked()) ? 2 : 0);
                checklistValues[5] = ((availableGoodRadio.isChecked()) ? 4 : 0);

            }
        });
    }

    private void addItemSelectedListenerToSpinner() {

        problemsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                checklistValues[6] = (problemsSpinner.getSelectedItem()).equals("Bad") ? -2 : 0;
                checklistValues[7] = (problemsSpinner.getSelectedItem()).equals("OK") ? 3 : 0;
                checklistValues[8] = (problemsSpinner.getSelectedItem()).equals("Good") ? 6 : 0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButtonOnClickListener() {
        startChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stoppedMilliseconds = 0;

                String chronoText = timeWaitingChronometer.getText().toString();
                String array[] = chronoText.split(":");

                if(array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 +
                            Integer.parseInt(array[1]) * 1000;

                }else if(array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 +
                            Integer.parseInt(array[1]) * 60 * 1000 +
                            Integer.parseInt(array[2]) * 1000;
                }

                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);

                secondsYouWaited = Long.parseLong(array[1]);

                updateTipBasedOnTimeWaited(secondsYouWaited);

                timeWaitingChronometer.start();
            }
        });

        pauseChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeWaitingChronometer.stop();
            }
        });

        resetChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());

                secondsYouWaited = 0;
            }
        });
    }

    private void updateTipBasedOnTimeWaited(long secondsYouWaited) {

        checklistValues[9] = (secondsYouWaited > 10) ? -2 : 0;

        setTipFromWaitressChecklist();
        updateTipAndFinalBill();
    }

    private void setTipFromWaitressChecklist() {
        int checklistTotal = 0;

        for(int item: checklistValues) {
            checklistTotal += item;
        }

        tipAmountET.setText(String.format("%.02f", checklistTotal * 0.01));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crazy_tip_calc, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP, tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}
