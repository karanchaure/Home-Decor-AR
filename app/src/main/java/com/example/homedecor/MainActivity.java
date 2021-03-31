package com.example.homedecor;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    MyAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the

    List<String> listmodel=new ArrayList<String>();
    List<Integer> position_list=new ArrayList<Integer>();
    List<File> file_list=new ArrayList<File>();
    Boolean check_downloadpresent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance().getReference().child("Models");

        recyclerView = findViewById(R.id.recycler1);
// karan code

        LinearLayoutManager layoutManager =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        // To display the Recycler view linearly
        recyclerView.setLayoutManager(layoutManager);

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Models> options
                = new FirebaseRecyclerOptions.Builder<Models>()
                .setQuery(mbase, Models.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new MyAdapter(options);
        // Connecting Adapter class with the Recycler view*/

        adapter.setOnItemClickListener(MainActivity.this);

        recyclerView.setAdapter(adapter);

    }


    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    //karan
    String name="out3";

    String extension="";


   public void onItemClick (int position)  {
      //  Toast.makeText(this, "Whatever click at position: " + position+"name: "+name, Toast.LENGTH_SHORT).show();
        name=(String)listmodel.get(position);
        extension=name+".glb";
        FirebaseApp.initializeApp(this);
//Firebase storage model reference were models are stored.
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(extension);
      // StorageReference modelRef1 = storage.getReference();

       Log.d("mymain","List:"+listmodel);
        //list
   /*   modelRef.listAll()*/
   /*            .addOnSuccessListener(new OnSuccessListener<ListResult>() {*/
   /*                @Override*/
   /*                public void onSuccess(ListResult listResult) {*/
   /*                    for (StorageReference prefix : listResult.getPrefixes()) {*/
   /*                        // All the prefixes under listRef.*/
   /*                        // You may call listAll() recursively on them.*/
   /*                        array1.add(prefix);*/
   /*                        Log.d("My","prefix1:"+prefix);*/

   /*                    }*/
   /*                    Log.d("My","array1:"+array1);*/
   /*                    for (StorageReference item : listResult.getItems()) {*/
   /*                        // All the items under listRef.*/
   /*                        array1.add(item);*/
   /*                        Log.d("My","item1:"+item);*/
   /*                    }*/
   /*                }*/
   /*            })*/
   /*            .addOnFailureListener(new OnFailureListener() {*/
   /*                @Override*/
   /*                public void onFailure(@NonNull Exception e) {*/
   /*                    // Uh-oh, an error occurred!*/
   /*                }*/
   /*            });*/
   /*    modelRef1.listAll()*/
   /*            .addOnSuccessListener(new OnSuccessListener<ListResult>() {*/
   /*                @Override*/
   /*                public void onSuccess(ListResult listResult) {*/
   /*                    for (StorageReference prefix : listResult.getPrefixes()) {*/
   /*                        // All the prefixes under listRef.*/
   /*                        // You may call listAll() recursively on them.*/



   /*                    }*/
   /*                    Log.d("My","array1:"+array1);*/
   /*                    for (StorageReference item : listResult.getItems()) {*/
   /*                        // All the items under listRef.*/
   /*                        array1.add(item);*/
   /*                        Log.d("My","item2:"+item);*/
   /*                    }*/
   /*                }*/
   /*            })*/
   /*            .addOnFailureListener(new OnFailureListener() {*/
   /*                @Override*/
   /*                public void onFailure(@NonNull Exception e) {*/
   /*                    // Uh-oh, an error occurred!*/
   /*                }*/
   /*            });*/

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

            findViewById(R.id.download)
                    .setOnClickListener(v -> {

                        try {
                            File file = File.createTempFile(name, "glb");
                           Log.d("My", "try: " + name);
                            position_list.add(position);
                            Log.d("My", "try:position list " + position_list);
                            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                   Log.d("My", "name: " + name);
                                    buildModel(file, name);

                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });

    //   FloatingActionButton dlt =(FloatingActionButton) findViewById(R.id.delete);


       arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

           AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
          // anchorNode.setRenderable(renderable);
           TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
           transformableNode.setParent(anchorNode);
           transformableNode.setRenderable(renderable);
//           findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View v) {
//                   //delete();
//                   removeAnchorNode( anchorNode);
//               }
//           });

               findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //delete();
                       removeAnchorNode( anchorNode);
                   }
               });
           arFragment.getArSceneView().getScene().addChild(anchorNode);

       });



    }

    @Override
    public void sendstring(String name) {
        listmodel.add(name);
    }

    public ModelRenderable renderable;


    public void buildModel(File file,String name) {
        Log.d("My","Model:"+name);
        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setScale(0.2f)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(this, "Model built "+name, Toast.LENGTH_SHORT).show();;

                    renderable = modelRenderable;
                });

    }

    private void removeAnchorNode(AnchorNode nodeToremove) {
        //Remove an anchor node
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
            nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);
            nodeToremove = null;
            Toast.makeText(this, "Test Delete - anchorNode removed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Test Delete - markAnchorNode was null", Toast.LENGTH_SHORT).show();
        }
    }

}
