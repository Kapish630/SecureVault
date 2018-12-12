package com.example.kapis.securevault;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kapis.securevault.Notes.Notes;
import com.example.kapis.securevault.Notes.NotesAdapter;
import com.example.kapis.securevault.Notes.NotesDBHelper;
import com.example.kapis.securevault.Notes.NotesDivider;
import com.example.kapis.securevault.Notes.NotesTouchListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class activity_NotesMain extends AppCompatActivity {

    private NotesAdapter mAdapter;
    private List<Notes> notesList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private NotesDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity_main);
        ButterKnife.bind(this);

        constraintLayout = findViewById(R.id.notesMain_Layout);
        recyclerView = findViewById(R.id.notesMain_RecView);

        db = new NotesDBHelper(this);

        notesList.addAll(db.getAllNotes());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.notesMain_FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotesDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new NotesDivider(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new NotesTouchListener(this,
                recyclerView, new NotesTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }


    // If the user clicks the Lock Button in the top right corner
    @OnClick(R.id.notesMain_LockBtn)
    public void lockApp(){
               // Create a dialog box where the user can choose whether they want to sign out or not
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to lock the app?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        startActivity(new Intent(activity_NotesMain.this, activity_LoginPage.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // If the user clicks the Lock Button in the top right corner
    @OnClick(R.id.notesMain_Logo)
    public void goHome(){
        startActivity(new Intent(activity_NotesMain.this, activity_Choice.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(mAdapter);
    }

    private void createNotes(String title, String body) {
        long id = db.insertNote(title,body);

        Notes notes = db.getNotes(id);

        if (notes != null) {
            notesList.add(0, notes);

            // refreshing the list
            mAdapter.notifyDataSetChanged();
            }
    }

    private void updateNotes(String title, String body, int position) {
        Notes n = notesList.get(position);
        n.setTitle(title);
        n.setBody(body);

        db.updateNote(n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);
    }

    private void deleteNote(int position) {
        db.deleteNote(notesList.get(position));

        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);
        }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNotesDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    public void showNotesDialog(final boolean shouldUpdate, final Notes notes, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.notes_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity_NotesMain.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputTitle = view.findViewById(R.id.notes_dialog_title);
        final EditText inputBody = view.findViewById(R.id.notes_dialog_body);

        TextView dialogHeader = view.findViewById(R.id.notes_header);
        dialogHeader.setText(!shouldUpdate ? getString(R.string.new_note) : getString(R.string.update_not_text));

        if (shouldUpdate && inputTitle != null && inputBody != null) {
            inputTitle.setText(notes.getTitle());
            inputBody.setText(notes.getBody());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                mAdapter.notifyDataSetChanged();
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputTitle.getText().toString())) {
                    Toast.makeText(activity_NotesMain.this, "Enter Title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(inputBody.getText().toString())) {
                    Toast.makeText(activity_NotesMain.this, "Enter Body!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && notes != null) {
                    updateNotes(inputTitle.getText().toString(),
                            inputBody.getText().toString(),
                            position);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    createNotes(inputTitle.getText().toString(), inputBody.getText().toString());
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

}