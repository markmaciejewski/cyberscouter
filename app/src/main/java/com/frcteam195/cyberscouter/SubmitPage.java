package com.frcteam195.cyberscouter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubmitPage extends AppCompatActivity {
    private Button button;
    private int g_matchScoutingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_page);

        button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToSummaryQuestions();

            }
        });

        button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMatch();

            }
        });

        button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMatchForReview();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        CyberScouterDbHelper mDbHelper = new CyberScouterDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        CyberScouterConfig cfg = CyberScouterConfig.getConfig(db);

        CyberScouterMatchScouting csm = CyberScouterMatchScouting.getCurrentMatch(db, TeamMap.getNumberForTeam(cfg.getRole()));

        if (null != csm) {
            TextView tv = findViewById(R.id.textView7);
            tv.setText(getString(R.string.tagMatch, csm.getTeamMatchNo()));
            tv = findViewById(R.id.textView9);
            tv.setText(getString(R.string.tagTeam, csm.getTeam()));

            g_matchScoutingID = csm.getMatchScoutingID();
        }
    }

    public void returnToSummaryQuestions() {
        this.finish();
    }

    public void submitMatch() {
        try {
            CyberScouterDbHelper mDbHelper = new CyberScouterDbHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            CyberScouterMatchScouting.submitMatch(db, g_matchScoutingID);
            notifyUser("Submit Scouting Report", "Scouting report sucessfully submitted!");
        } catch (Exception e) {
            MessageBox.showMessageBox(this, "Submit Match Failed Alert", "submitMatch",
                    "Update of UploadStatus and ScoutingStatus failed!\n\n" +
                            "The error is:\n" + e.getMessage());
        }

    }

    public void submitMatchForReview() {
        try {
            CyberScouterDbHelper mDbHelper = new CyberScouterDbHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            CyberScouterMatchScouting.submitMatchForReview(db, g_matchScoutingID);
            notifyUser("Submit Scouting Report for Review", "Scouting report sucessfully submitted for review!");
        } catch (Exception e) {
            MessageBox.showMessageBox(this, "Submit Match Failed Alert", "submitMatchForReview",
                    "Update of UploadStatus and ScoutingStatus failed!\n\n" +
                            "The error is:\n" + e.getMessage());
        }
    }

    public void returnToScoutingPage() {
        Intent intent = new Intent(this, ScoutingPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void notifyUser(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                returnToScoutingPage();
            }
        });
        messageBox.show();

    }
}
