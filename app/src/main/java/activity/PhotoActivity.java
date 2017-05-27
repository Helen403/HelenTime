package activity;

import android.content.Intent;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.example.snoy.helen.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterPhoto;
import base.HBaseActivity;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Helen on 2017/5/24.
 */
public class PhotoActivity extends HBaseActivity {


    private AdapterPhoto adapterPhoto;

    private ArrayList<String> selectedPhotos = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_photo;
    }

    @Override
    public void findViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapterPhoto = new AdapterPhoto(this, selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(adapterPhoto);
    }

    @Override
    public void initData() {
        contentView.findViewById(R.id.photo_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(4)
                        .start(PhotoActivity.this);
            }
        });
    }

    @Override
    public void setListeners() {

    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            adapterPhoto.notifyDataSetChanged();
        }
    }

}
