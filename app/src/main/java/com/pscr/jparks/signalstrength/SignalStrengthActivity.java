package com.pscr.jparks.signalstrength;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
/*
TS 27.007 8.5
Defined values
<rsrp>:
0 -113 dBm or less
1 -111 dBm
2...30 -109... -53 dBm
31 -51 dBm or greater
99 not known or not detectable
 */

/*
The parts[] array will then contain these elements:

part[0] = "Signalstrength:"  _ignore this, it's just the title_
parts[1] = GsmSignalStrength
parts[2] = GsmBitErrorRate
parts[3] = CdmaDbm
parts[4] = CdmaEcio
parts[5] = EvdoDbm
parts[6] = EvdoEcio
parts[7] = EvdoSnr
parts[8] = LteSignalStrength
parts[9] = LteRsrp
parts[10] = LteRsrq
parts[11] = LteRssnr
parts[12] = LteCqi
parts[13] = gsm|lte
parts[14] = _not reall sure what this number is_
 */

public class SignalStrengthActivity  extends Activity {

    // How do I release the Listener when app closes???????
    SignalStrengthListener signalStrengthListener;

    TextView signalStrengthTextView, signalStrengthTextView2;
    TextView cellIDTextView;
    TextView cellMccTextView;
    TextView cellMncTextView;
    TextView cellPciTextView;
    TextView cellTacTextView;

    List<CellInfo> cellInfoList;
    int cellSig, cellID, cellMcc, cellMnc, cellPci, cellTac = 0;

    TelephonyManager tm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup content stuff
        this.setContentView(R.layout.signal_strength);

        signalStrengthTextView = (TextView) findViewById(R.id.signalStrengthTextView);
        signalStrengthTextView2 = (TextView) findViewById(R.id.signalStrengthTextView2);
        cellIDTextView = (TextView) findViewById(R.id.cellIDTextView);
        cellMccTextView = (TextView) findViewById(R.id.cellMccTextView);
        cellMncTextView = (TextView) findViewById(R.id.cellMncTextView);
        cellPciTextView = (TextView) findViewById(R.id.cellPciTextView);
        cellTacTextView = (TextView) findViewById(R.id.cellTacTextView);


        //start the signal strength listener
        signalStrengthListener = new SignalStrengthListener();

        ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(signalStrengthListener, SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        try {
            cellInfoList = tm.getAllCellInfo();
        } catch (Exception e) {
            Log.d("SignalStrength", "+++++++++++++++++++++++++++++++++++++++++ null array spot 1: " + e);

        }


        try {
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoLte) {
                    // cast to CellInfoLte and call all the CellInfoLte methods you need
                    // gets RSRP cell signal strength:
                    cellSig = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();

                    // Gets the LTE cell indentity: (returns 28-bit Cell Identity, Integer.MAX_VALUE if unknown)
                    cellID = ((CellInfoLte) cellInfo).getCellIdentity().getCi();

                    // Gets the LTE MCC: (returns 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown)
                    cellMcc = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();

                    // Gets theLTE MNC: (returns 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown)
                    cellMnc = ((CellInfoLte) cellInfo).getCellIdentity().getMnc();

                    // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                    cellPci = ((CellInfoLte) cellInfo).getCellIdentity().getPci();

                    // Gets the LTE TAC: (returns 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown)
                    cellTac = ((CellInfoLte) cellInfo).getCellIdentity().getTac();

                }

            }
        } catch (Exception e) {
            Log.d("SignalStrength", "++++++++++++++++++++++ null array spot 2: " + e);
        }

    }



    @Override
    public void onPause() {
        super.onPause();

        try{
            if(signalStrengthListener != null){tm.listen(signalStrengthListener, SignalStrengthListener.LISTEN_NONE);}
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void onDestroy() {
        super.onDestroy();

        try{
            if(signalStrengthListener != null){tm.listen(signalStrengthListener, SignalStrengthListener.LISTEN_NONE);}
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    private class SignalStrengthListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {

            //++++++++++++++++++++++++++++++++++

            ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(signalStrengthListener, SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            String ltestr = signalStrength.toString();
            String[] parts = ltestr.split(" ");
            String cellSig2 = parts[9];

            try {
                cellInfoList = tm.getAllCellInfo();
                for (CellInfo cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellInfoLte) {
                        // cast to CellInfoLte and call all the CellInfoLte methods you need
                        // gets RSRP cell signal strength:
                        cellSig = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();

                        // Gets the LTE cell identity: (returns 28-bit Cell Identity, Integer.MAX_VALUE if unknown)
                        cellID = ((CellInfoLte) cellInfo).getCellIdentity().getCi();

                        // Gets the LTE MCC: (returns 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown)
                        cellMcc = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();

                        // Gets theLTE MNC: (returns 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown)
                        cellMnc = ((CellInfoLte) cellInfo).getCellIdentity().getMnc();

                        // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                        cellPci = ((CellInfoLte) cellInfo).getCellIdentity().getPci();

                        // Gets the LTE TAC: (returns 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown)
                        cellTac = ((CellInfoLte) cellInfo).getCellIdentity().getTac();

                    }
                }
            } catch (Exception e) {
                Log.d("SignalStrength", "+++++++++++++++++++++++++++++++ null array spot 3: " + e);
            }

            signalStrengthTextView.setText(String.valueOf(cellSig));
            signalStrengthTextView2.setText(String.valueOf(cellSig2));
            cellIDTextView.setText(String.valueOf(cellID));
            cellMccTextView.setText(String.valueOf(cellMcc));
            cellMncTextView.setText(String.valueOf(cellMnc));
            cellPciTextView.setText(String.valueOf(cellPci));
            cellTacTextView.setText(String.valueOf(cellTac));

            super.onSignalStrengthsChanged(signalStrength);

            //++++++++++++++++++++++++++++++++++++

        }
    }

}
