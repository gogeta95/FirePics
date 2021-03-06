package com.google.firebase.quickstart.firebasestorage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by saurabh on 25/9/16.
 */

public class MyFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<String,ImageViewHolder> {
    public static final String TAG=MyFirebaseRecyclerAdapter.class.getSimpleName();
    private Context context;
    public MyFirebaseRecyclerAdapter(Class<String> modelClass, int modelLayout, Class<ImageViewHolder> viewHolderClass, DatabaseReference ref,Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context=context;
    }

    @Override
    protected void populateViewHolder(final ImageViewHolder viewHolder, final String model, final int position) {
        Glide.with(context).load(model).placeholder(R.drawable.default_placeholder).into(viewHolder.imageView);
        Log.d(TAG, "populateViewHolder: "+model);
        viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //delete file from storage.
                FirebaseStorage.getInstance().getReferenceFromUrl(model).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //delete file reference from database.
                        getRef(viewHolder.getAdapterPosition()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "File Deleted.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Deleted: " + model);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed to delete file!!");
                    }
                });
                return true;
            }
        });
    }
}
