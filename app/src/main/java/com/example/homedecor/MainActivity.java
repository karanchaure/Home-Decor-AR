package com.example.homedecor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;


import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {
    String TAG = "MainActivity";
    private RecyclerView recyclerView;
    MyAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the
    ProgressDialog mProgressDialog;
    List<String> listmodel = new ArrayList<String>();
    List<String> listmodel2 = new ArrayList<String>();
    List<Integer> position_list = new ArrayList<Integer>();
    List<File> file_list = new ArrayList<File>();
    Boolean check_downloadpresent;
    ArFragment arFragment;
    public ModelRenderable renderable, sample_model = null, last_renderable, sample_model_copy;
    AnchorNode last_anchor_node = null, myanchornode;
    Node hitNode = null;
    SeekBar sb_size;
    Dictionary<ModelRenderable, String> renderable_id_dic = new Hashtable<ModelRenderable, String>();
    Dictionary<String, ModelRenderable> save_renderable_dic = new Hashtable<String, ModelRenderable>();
    Dictionary<String, Integer> filename_image_id_dic = new Hashtable<String, Integer>();
    String file_name = null;
    int x = 0;
    int iterate = 1;

    Button download, change;
    String name = "";
    String extension = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb_size = (SeekBar) findViewById(R.id.sb_size);
        sb_size.setEnabled(false);

        download = findViewById(R.id.download);
        change = findViewById(R.id.change);
        download.setVisibility(View.INVISIBLE);
        change.setVisibility(View.INVISIBLE);
        //    d(TAG, "onCreate: b1"+//b1.getText());
        //  d(TAG, "onCreate: b1"+percentage.getText());
        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance().getReference().child("Models");

        recyclerView = findViewById(R.id.recycler1);
// karan code

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
        filename_image_id_dic.put("woodentable", 3);
        filename_image_id_dic.put("chair", 2);
        filename_image_id_dic.put("bedsidetable", 3);
//        findViewById(R.id.shape).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //shape
//
//
//                    shape();
//
//
//            }
//        });
//        findViewById(R.id.shape2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //shape
//
//
//                shape2();
//
//
//            }
//        });

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change
                d(TAG, "modellist2=" + listmodel2);

                try {
                    if (hitNode != null) {
                        int max = filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable()));
                        if (iterate <= max) {
                            d(TAG, "iterate=" + iterate + "max=" + filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable())) + "name=" + renderable_id_dic.get(hitNode.getRenderable()));

                            //   String name=renderable_id_dic.get(hitNode.getRenderable());

                            iterate = change(filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable()))
                                    , renderable_id_dic.get(hitNode.getRenderable()));
                        } else {
                            iterate = 1;
                            d(TAG, "else iterate=" + iterate + "max=" + filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable())) + "name=" + renderable_id_dic.get(hitNode.getRenderable()));
                            iterate = change(filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable()))
                                    , renderable_id_dic.get(hitNode.getRenderable()));
                        }
                    } else {
//                            int max=filename_image_id_dic.get(renderable_id_dic.get(last_renderable));
//                            if(iterate <=max) {
//                                d(TAG, "iterate=" + iterate + "max=" + filename_image_id_dic.get(renderable_id_dic.get(last_renderable)) + "name=" + renderable_id_dic.get(last_renderable));
//
//                                //   String name=renderable_id_dic.get(hitNode.getRenderable());
//
//                                iterate = change(filename_image_id_dic.get(renderable_id_dic.get(last_renderable))
//                                        , renderable_id_dic.get(last_renderable));
//                            }
//                            else{
//                                iterate=1;
//                                d(TAG,"else iterate="+iterate+"max="+filename_image_id_dic.get(renderable_id_dic.get(last_renderable))+"name="+renderable_id_dic.get(last_renderable));
//                                iterate= change(filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable()))
//                                        , renderable_id_dic.get(last_renderable));
//                            }
////
                        d(TAG, "PLease select Node");
                    }

                } catch (Exception e) {
                    d(TAG, "" + e);
                }
            }
        });
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.getArSceneView().getScene().addOnPeekTouchListener((hitTestResult, motionEvent) -> handleOnTouch(hitTestResult, motionEvent));

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                //Delete the Anchor if it exists
                d(TAG, "Deleting anchor");

//                int currentAnchorIndex;
//                if (numberOfAnchors < 1 ) {
//                    Toast.makeText(LineViewMainActivity.this, "All nodes deleted", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                removeAnchorNode(hitNode);


                //Remove the line if it exists also
                //  removeLine(nodeForLine);
            }
        });

        sb_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // mySize = progress;
                if (hitNode != null) {

                    d(TAG, "onProgressChanged: anchornode:" + hitNode.getParent() + "pROGRESS:" + progress);
                    TransformableNode temp;
                    temp = (TransformableNode) hitNode.getParent();
                    temp.setLocalScale(new Vector3(progress,progress,progress));
                    //    last_anchor_node.getLocalScale(new );


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }







    private void handleOnTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
        d(TAG, "handleOnTouch");
        // First call ArFragment's listener to handle TransformableNodes.
        arFragment.onPeekTouch(hitTestResult, motionEvent);

        //We are only interested in the ACTION_UP events - anything else just return
//        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
//            return;
//        }
        d(TAG,"hitresultnode="+hitTestResult.getNode());
        // Check for touching a Sceneform node
        if (hitTestResult.getNode() != null) {
          //  Log.d(TAG, "handleOnTouch hitTestResult.getNode() != null");
            //Toast.makeText(LineViewMainActivity.this, "hitTestResult is not null: ", Toast.LENGTH_SHORT).show();
             hitNode = hitTestResult.getNode();
            d(TAG, "handleOnTouch: getparent:"+hitNode.getParent()+" Children:"+hitNode.getChildren());
            d(TAG,"handleOnTouch: renderable ="+hitNode.getRenderable());
            if(filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable())) != null){
                change.setVisibility(View.VISIBLE);
            }else{
                change.setVisibility(View.INVISIBLE);

            }           // current_anchor_node = (AnchorNode) hitNode;
        }

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

    public Boolean checknamepresent(String s){
        if(save_renderable_dic.get(s) !=  null){
           return true;
        }else{
            return false;
        }
    }

   public void onItemClick (int position)  {
      //  Toast.makeText(this, "Whatever click at position: " + position+"name: "+name, Toast.LENGTH_SHORT).show();
       arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        name=(String)listmodel.get(position);
        if(checknamepresent(name)){
            if(filename_image_id_dic.get(name)!=null){
                change.setVisibility(View.VISIBLE);
            }else{
                change.setVisibility(View.INVISIBLE);
            }
       download.setVisibility(View.INVISIBLE);
                arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                d(TAG,"inside onitemclick  found key in save_renderable_dic"+save_renderable_dic+"name"+name);
                AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                // anchorNode.setRenderable(renderable);
                last_anchor_node=anchorNode;
                TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                transformableNode.setParent(anchorNode);
                transformableNode.getScaleController().setMaxScale(0.2f);
                transformableNode.getScaleController().setMinScale(0.5f);
                transformableNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
                sample_model=save_renderable_dic.get(name);
                sample_model_copy=sample_model.makeCopy();
                last_renderable=sample_model_copy;
                transformableNode.setRenderable(sample_model_copy);
                renderable_id_dic.put(sample_model_copy,name);
                sb_size.setEnabled(true);
                transformableNode.select();
                 //   sb_size.setEnabled(true);
                d(TAG, "onItemClick:trans node= "+transformableNode+"renderable"+transformableNode.getRenderable());
                d(TAG, "onItemClick:last node= "+last_anchor_node+"renderable"+last_anchor_node.getRenderable());

                arFragment.getArSceneView().getScene().addChild(anchorNode);

          });

        }
        else {
            download.setVisibility(View.VISIBLE);
            try{
            if(filename_image_id_dic.get(name)!=null){
                change.setVisibility(View.VISIBLE);
            }else{
                change.setVisibility(View.INVISIBLE);
            }}catch (Exception e){
                d(TAG, "onItemClick: exception"+e);
            }
            extension = name + ".glb";
            FirebaseApp.initializeApp(this);
//Firebase storage model reference were models are stored.
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference modelRef = storage.getReference().child(extension);
            // StorageReference modelRef1 = storage.getReference();
            findViewById(R.id.download)
                    .setOnClickListener(v -> {
                        mProgressDialog= new ProgressDialog(MainActivity.this);
                   //     mProgressDialog.setProgress(0);
                        mProgressDialog.show();
                      //  mProgressDialog.setContentView(R.layout.progressdialog);
                     //   mProgressDialog.getWindow().setBackgroundDrawableResource(
                   //             android.R.color.transparent
                    //    );
                        try {
                            File file = File.createTempFile(name, "glb");d(TAG, "name: " + name);
                            position_list.add(position);
                            d(TAG, "try:position list " + position_list);
                            d(TAG, "List:" + listmodel);
                            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    d(TAG, "on success name: " + name);
                                    sample_model=buildModel(file, name);
                                    mProgressDialog.dismiss();
                                    download.setVisibility(View.INVISIBLE);
                               //     d(TAG,"inside onitemclick  notfound key in save_renderable_dic"+save_renderable_dic+"name"+name);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressDialog.dismiss();
                                    d(TAG, "onFailure: failure occured");
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull  FileDownloadTask.TaskSnapshot snapshot) {

                                        double progress = (100* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                    d(TAG,"Downloaded "+((int)progress)+"%");

                                        mProgressDialog.setMessage("Downloading "+((int)progress)+"%");
                                   //     mProgressDialog.setProgress((int) progress);
                                  //  d(TAG, "onProgress: textview"+percentage.getText());
                                        //   percentage.setText("Downloading "+((int)progress)+"%");
                                }
                            })
                            ;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });

            //   FloatingActionButton dlt =(FloatingActionButton) findViewById(R.id.delete);



                    if(sample_model != null) {
                        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                    // anchorNode.setRenderable(renderable);
                    last_anchor_node = anchorNode;
                    TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                    transformableNode.setParent(anchorNode);

                    transformableNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
                    d(TAG, "onItemClick:renderable:" + renderable);
                    sample_model = renderable;
                    d(TAG, "onItemClick: sample model:" + sample_model);
                    sample_model_copy = sample_model.makeCopy();
                    last_renderable = sample_model_copy;
                    transformableNode.setRenderable(sample_model_copy);
                    renderable_id_dic.put(sample_model_copy, name);
                    transformableNode.select();
                   sb_size.setEnabled(true);

                    d(TAG, "onItemClick:trans node= " + transformableNode + "renderable" + transformableNode.getRenderable());
                    d(TAG, "onItemClick:last node= " + last_anchor_node + "renderable" + last_anchor_node.getRenderable());

                    arFragment.getArSceneView().getScene().addChild(anchorNode);
                    });
                }

        }


    }

    @Override
    public void sendstring(String name) {
        listmodel.add(name);
    }


    //
    public  void shape() {

        ModelRenderable.builder()
                .setSource(this, Uri.parse("truck.sfb"))
                .build()
                .thenAccept(renderable -> {
                    Toast.makeText(this, "Indise", Toast.LENGTH_SHORT).show();
                    sample_model = renderable;
                    Toast.makeText(this, "Indise aftr", Toast.LENGTH_SHORT).show();
                })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load model_node renderable", Toast.LENGTH_LONG);
                            //    toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;

                        });


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor= hitResult.createAnchor();
            d(TAG,"node="+anchor);
            AnchorNode anchornode = new AnchorNode(anchor);
            d(TAG,"last node ="+last_anchor_node);
            anchornode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
            d(TAG,"Trans node ="+transformableNode);
            transformableNode.setParent(anchornode);
            d(TAG,"sample model ="+sample_model);
            sample_model_copy=sample_model.makeCopy();
            d(TAG,"sample model copy ="+sample_model_copy);
            renderable_id_dic.put(sample_model_copy,file_name="truck" );
            transformableNode.setRenderable(sample_model_copy);
          //  ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
            transformableNode.select();
            d(TAG,"renderable dic"+renderable_id_dic);

//            transformableNode.setOnTapListener((Node.OnTapListener) this);
    //        arFragment.getArSceneView().getScene().addOnUpdateListener((Scene.OnUpdateListener) this);
        });
    }

    private void shape2() {
        ModelRenderable.builder()
                .setSource(this, RenderableSource.builder().setSource(
                        this,
                        Uri.parse("bedsidetable.glb"),
                        RenderableSource.SourceType.GLB)
                     //   .setScale(0.5f)  // Scale the original model to 50%.
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
               // .setRegistryId("bedsidetable")
                .build()
                .thenAccept(renderable -> {sample_model = renderable;
                       d(TAG,"inside shape 2 sample_model2="+sample_model);
                }
                )
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load renderable ", Toast.LENGTH_LONG);
                         //   toast.setGravity(Gravity.CENTER, 0, 0);
                            d(TAG,"unable to render");
                            toast.show();
                            return null;
                        });
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor= hitResult.createAnchor();
            d(TAG,"node="+anchor);
            AnchorNode anchornode = new AnchorNode(anchor);
            d(TAG,"last current node ="+last_anchor_node);
            anchornode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
            d(TAG,"Trans node ="+transformableNode);
            transformableNode.setParent(anchornode);
//            d(TAG,"sample model ="+sample_model2);
//            sample_model_copy=sample_model2.makeCopy();
            d(TAG,"sample model copy ="+sample_model_copy);
            renderable_id_dic.put(sample_model_copy,file_name="bedsidetable" );
            transformableNode.setRenderable(sample_model_copy);
            //  ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
            transformableNode.select();
            d(TAG,"renderable dic"+renderable_id_dic);
   });
    }

    public int change(int selected_rendarable_count,String selected_rendarable_name ){

    //   int selected_rendarable_count= filename_image_id_dic.get(renderable_id_dic.get(hitNode.getRenderable()));
    //   String selected_rendarable_name=renderable_id_dic.get(hitNode.getRenderable());
        if(iterate == selected_rendarable_count){
            d(TAG,"iterate ==max "+iterate);
            String image_name = selected_rendarable_name + iterate;
            d(TAG,"image_name="+image_name);
            int redID = getResources().getIdentifier(image_name, "drawable", getPackageName());
            Texture.builder().setSource(this, redID).build().thenAccept(texture -> {
                MaterialFactory.makeOpaqueWithTexture(this, texture)
                        .thenAccept(
                                material -> {

                                    d(TAG, "inside change else change sample =" + sample_model);
                                    //sample_model2 = sample_model.makeCopy();
                                    Toast.makeText(this, "Indise if x=" + x, Toast.LENGTH_SHORT).show();
                                    // sample_model.getMaterial().setTexture(MaterialFactory.MATERIAL_TEXTURE, texture);
                                    // sample_model2= (ModelRenderable) current_anchor_node.getRenderable().makeCopy();
                                    renderable = (ModelRenderable) hitNode.getRenderable();
//                                    if(hitNode==null) {
//                                        renderable = last_renderable;
//                                    }
//                                    else{
//                                        renderable = (ModelRenderable) hitNode.getRenderable();
//                                    }
                                    d(TAG, "inside change else renderabel ID= " + renderable);
                                    renderable.setMaterial(material);
                                })
                        .exceptionally(
                                throwable -> {
                                    Toast toast =
                                            Toast.makeText(this, "if Unable to load model_node renderable", Toast.LENGTH_LONG);
                                    //    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    return null;

                                });

            });
            return 1;
        }else {
            String image_name = selected_rendarable_name + iterate;
            d(TAG,"image_name="+image_name);
            int redID = getResources().getIdentifier(image_name, "drawable", getPackageName());
            Texture.builder().setSource(this, redID).build().thenAccept(texture -> {
                MaterialFactory.makeOpaqueWithTexture(this, texture)
                        .thenAccept(
                                material -> {

                                    d(TAG, "inside change else change sample =" + sample_model);
                                    //sample_model2 = sample_model.makeCopy();
                                    Toast.makeText(this, "Indise if x=" + x, Toast.LENGTH_SHORT).show();
                                    // sample_model.getMaterial().setTexture(MaterialFactory.MATERIAL_TEXTURE, texture);
                                    // sample_model2= (ModelRenderable) current_anchor_node.getRenderable().makeCopy();
                                   renderable = (ModelRenderable) hitNode.getRenderable();
                                    //afterchange
//                                    if(hitNode==null) {
//                                        renderable = last_renderable;
//                                    }
//                                    else{
//                                        renderable = (ModelRenderable) hitNode.getRenderable();
//                                    }
                                    d(TAG, "inside change else renderabel ID= " + renderable);
                                    renderable.setMaterial(material);
                                })
                        .exceptionally(
                                throwable -> {
                                    Toast toast =
                                            Toast.makeText(this, "if Unable to load model_node renderable", Toast.LENGTH_LONG);
                                    //    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    return null;

                                });

            });
            return iterate+1;
        }

   }

    public ModelRenderable buildModel(File file,String name) {
        d(TAG,"Model:"+name);
        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                //.setScale(0.2f)
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

                    d(TAG, "buildModel: successfull Model:"+renderable);
                    save_renderable_dic.put(name,renderable);
                });
        sample_model=null;
        return renderable;
    }

//    private void removeAnchorNode(AnchorNode nodeToremove) {
//        //Remove an anchor node
//        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
//        if (nodeToremove != null) {
//            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
//            nodeToremove.getAnchor().detach();
//            nodeToremove.setParent(null);
//            nodeToremove = null;
//            Toast.makeText(this, "Test Delete - anchorNode removed", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Test Delete - markAnchorNode was null", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void removeAnchorNode(Node nodeToremove) {
        //Remove an anchor node
        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
          //  anchorNodeList.remove(nodeToremove);
         //   nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);
            nodeToremove = null;
            d(TAG, "removeAnchorNode: Deleted node successfully");
          //  numberOfAnchors--;
            //Toast.makeText(LineViewMainActivity.this, "Test Delete - markAnchorNode removed", Toast.LENGTH_SHORT).show();
        } else if(last_anchor_node !=null){
            arFragment.getArSceneView().getScene().removeChild(last_anchor_node);
            //  anchorNodeList.remove(nodeToremove);
               last_anchor_node.getAnchor().detach();
            last_anchor_node.setParent(null);
            last_anchor_node = null;
            d(TAG, "removeAnchorNode: Deleted node successfully");
            //  numberOfAnchors--;
        }else {
            Toast.makeText(MainActivity.this, "Delete - no node selected! Touch a node to select it.", Toast.LENGTH_SHORT).show();
        }
    }

}
