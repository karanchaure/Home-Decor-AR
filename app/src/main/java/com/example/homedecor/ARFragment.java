package com.example.homedecor;



import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ARFragment extends AppCompatActivity  {

    String name="";
    String extension="";

    //constructor
    ARFragment(String name){
        this.name=name;
    }

    public ARFragment() {

    }

    public void OnCreate(Bundle savedInstanceState){





            FirebaseApp.initializeApp(this);

            //karan
            extension=name+".glb";

            FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("AR",""+storage);
            StorageReference modelRef = storage.getReference().child(extension);
        Log.d("AR",""+modelRef);
            ArFragment arFragment = (ArFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.arFragment);
        Log.d("AR","name="+" ,extension="+extension);




                        try {

                            Log.d("AR","name"+name);

                            File file = File.createTempFile(name, "glb");
                            Log.d("AR","try block file"+file);

                            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {



                                @Override
                                 public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("AR","onsuccess file");
                                    buildModel(file,extension);


                                }

                            });

                        } catch (IOException e) {
                            Log.d("AR","catch");
                            e.printStackTrace();

                        }



            arFragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                anchorNode.setRenderable(renderable);
                arFragment.getArSceneView().getScene().addChild(anchorNode);

            });

    }

    private ModelRenderable renderable;

    private void buildModel(File file, String extension) {

        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(this, "Model built"+extension, Toast.LENGTH_SHORT).show();;
                    renderable = modelRenderable;
                });

    }

}
