package com.app.abby.perfectweather.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abby.perfectweather.R;
import com.app.abby.perfectweather.activity.ADetailActivity;
import com.app.abby.perfectweather.activity.MainActivity;
import com.app.abby.perfectweather.activity.SettingActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment2 extends Fragment {
    private static String ARG_PARAM3 = "param3";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String ARG_PARAM1 = "param1";
    private static String ARG_PARAM2 = "param2";
    LinearLayout setting,about,share,area;
    View view;
    TextView tv_username;
    TextView tv_userid;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private OnFragmentInteractionListener mListener;

    public BlankFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment2 newInstance(String param1, String param2 , String param3) {
        BlankFragment2 fragment = new BlankFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        initalView();
        //TextView textView = (TextView)view.findViewById(R.id.f2_text1);
        //textView.setText("hahha");
        setLisener();
        return view;
    }

    private void initalView() {
        tv_userid = (TextView) view.findViewById(R.id.user_id);
        tv_username = (TextView) view.findViewById(R.id.user_name);
        tv_username.setText(mParam1);
        tv_userid.setText(mParam2);
    }

    private void setLisener() {
        share=(LinearLayout) view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items3 = new String[]{"QQ分享", "QQ空间分享"};//创建item
                AlertDialog alertDialog3 = new AlertDialog.Builder(getContext())
                        .setTitle("选择您的分享")
                        .setIcon(R.drawable.share)
                        .setItems(items3, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    onClickShare();
                                }
                                if(i==1){
                                    shareToQQzone();
                                }
                                Toast.makeText(getContext(), "点的是：" + items3[i] + i, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "这是取消按钮", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog3.show();
            }
        });
        setting=(LinearLayout) view.findViewById(R.id.user_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
            }
        });
        area = (LinearLayout)view.findViewById(R.id.area);
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                AlertDialog alertDialog1 = new AlertDialog.Builder(context)
                        .setTitle("地区收藏")//标题
                        .setMessage(mParam3)//内容
                        .setIcon(R.mipmap.weather_icon)//图标
                        .create();
                alertDialog1.show();
            }
        });
        about = (LinearLayout) view.findViewById(R.id.user_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                AlertDialog alertDialog1 = new AlertDialog.Builder(context)
                        .setTitle("关于")//标题
                        .setMessage("移动应用开发课最终答辩项目：完美天气\n" +
                                "组员：张力天，孙文举，赵淑钰，朱子潇，莫日根呼\n" +
                                "所用时间：三周\n" +
                                "版本号：2.0")//内容
                        .setIcon(R.mipmap.weather_icon)//图标
                        .create();
                alertDialog1.show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //qq分享
    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
                "http://blog.csdn.net/DickyQie/article/list/1");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "切切歆语");
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        Tencent mTencent = Tencent.createInstance("1108071510",
                getContext());
        mTencent.shareToQQ((Activity) getContext(), params, new BaseUiListener1());
    }
    //回调接口  (成功和失败的相关操作)
    private class BaseUiListener1 implements IUiListener {
        @Override
        public void onComplete(Object response) {
            doComplete(response);
        }

        protected void doComplete(Object values) {
        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    }

    private void shareToQQzone() {
        try {
            final Bundle params = new Bundle();
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "切切歆语");
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "sss");
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
                    "http://blog.csdn.net/DickyQie/article/list/1");
            ArrayList<String> imageUrls = new ArrayList<String>();
            imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
            params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,
                    QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            Tencent mTencent = Tencent.createInstance("1108071510",
                    getContext());
            mTencent.shareToQzone((Activity) getContext(), params,
                    new BaseUiListener1());
        } catch (Exception e) {
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
