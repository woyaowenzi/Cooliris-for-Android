package com.gtx.cooliris.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gtx.cooliris.entity.Image;
import com.gtx.cooliris.imagecache.ImageFetcher;
import com.gtx.cooliris.imagecache.ImageWorker;
import com.gtx.cooliris.imagecache.Utils;
import com.gtx.cooliris.widget.ImageViewEx;
import com.gtx.cooliris.R;

/**
 * This fragment will populate the children of the ViewPager from {@link FavoriteImageActivity}.
 */
public class ImageDetailFragment extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "resId";
    
    private String mImageUrl;
    private int	mIndex;
    private Image m_girlImage = null;
    
    private ImageViewEx mImageView;
    private ImageFetcher mImageFetcher;

	// TODO: To be removed.
    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imageUrl);
        f.setArguments(args);

        return f;
    }
    
    public static ImageDetailFragment newInstance(int index) {  	
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putInt(IMAGE_DATA_EXTRA, index);
        f.setArguments(args);

        return f;
    }
    
    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageDetailFragment() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link ImageDetailFragment#newInstance(String)} to create this fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
        
        mIndex = getArguments() != null ? getArguments().getInt(IMAGE_DATA_EXTRA) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageViewEx) v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: optimize
        // Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        if (FavoriteImageActivity.class.isInstance(getActivity())) {
        	mImageFetcher = ((FavoriteImageActivity) getActivity()).getImageFetcher();
        	
        	mImageUrl = (String)mImageFetcher.getAdapter().getItem(mIndex);
        	m_girlImage = (Image)mImageFetcher.getAdapter().getOriginalItem(mIndex);
        	mImageView.setDataSource(m_girlImage);
        	
        	mImageFetcher.loadImage(mImageUrl, mImageView);
        }
        else if (ImageDetailActivity.class.isInstance(getActivity())) {
            mImageFetcher = ((ImageDetailActivity) getActivity()).getImageFetcher();
            
            mImageUrl = (String)mImageFetcher.getAdapter().getItem(mIndex);
        	m_girlImage = (Image)mImageFetcher.getAdapter().getOriginalItem(mIndex);
        	mImageView.setDataSource(m_girlImage);
        	
            mImageFetcher.loadImage(mImageUrl, mImageView);
        }        

        // Pass clicks on the ImageView to the parent activity to handle
        if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
            mImageView.setOnClickListener((OnClickListener) getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageView != null) {
            // Cancel any pending image work
            ImageWorker.cancelWork(mImageView);
            mImageView.setImageDrawable(null);
        }
    }
    
    public ImageView getImageView()
	{
		return mImageView;
	}
}
