package com.ws.support.base.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ws.support.base.fragment.BaseFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;


public class BaseDataBindingActivity2<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity {

    protected static final int REQUEST_CODE_PERMISSION = 10000;

    VM viewModel;
    DB dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<VM> vmClass = initViewModelClass();
        if (vmClass != null){
            viewModel = getDefaultViewModelProviderFactory().create(vmClass);
        }

        int layoutID = getLayoutId();
        if (layoutID != 0) {
            dataBinding = DataBindingUtil.setContentView(this, layoutID);
            dataBinding.setLifecycleOwner(this);
            initBinding(dataBinding);
        }
    }

    protected Class<VM> initViewModelClass() {
        return null;
    }

    protected int getLayoutId() {
        return 0;
    }
    protected void initBinding(DB dataBinding) {}

    public VM getViewModel() {
        return viewModel;
    }

    public DB getDataBinding() {
        return dataBinding;
    }

    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission) {
        if (!hasPermission(permission)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE_PERMISSION);
        } else {
            permissionGranted(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted(permissions[i]);
            } else {
                permissionDenied(permissions[i]);
            }
        }
    }

    protected void permissionGranted(String permission) {
    }
    protected void permissionDenied(String permission) {
    }


    public void runDelay(Runnable runnable, long delay) {
        getWindow().getDecorView().postDelayed(runnable,delay);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if (list.isEmpty()) {
            super.onBackPressed();
        }
        for (Fragment f : list) {
            if (f instanceof BaseFragment && ((BaseFragment) f).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public void clearCurrentFocus() {
        if (getCurrentFocus()!= null) getCurrentFocus().clearFocus();
    }

    public void requestFocus(final View v) {
        runDelay(() -> v.requestFocus(), 10);
    }

    public void requestFocus(final EditText v) {
        runDelay(() -> {
            v.requestFocus();
            v.selectAll();
        }, 10);
    }



    /**
     * 检查EditText是否为空
     * @param view
     * @param id
     * @return    为空返回TRUE。
     */
    public boolean checkInput(EditText view, int id) {
        String editText=view.getText().toString();
        if(TextUtils.isEmpty(editText)){
            //ToastUtils.shortError(id);
            return true;
        }
        return false;
    }


    /**
     * 防止按钮连续点击 （间隔500毫秒）
     */
    private static long lastClickTime = 0;
    public boolean isFastClick(){
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) < 500) {
            return true;
        }
        lastClickTime = curClickTime;
        return false;
    }

    public <T> T startFragment(int resid, Class<T> fname, String tag, Bundle bundle, boolean addStack) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (tag == null) {
            tag = fname.getName();
        }
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, fname.getName(), bundle);
            ft.add(resid, fragment, tag);
        } else {
            ft.show(fragment);
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        fm.executePendingTransactions();
        return (T) fragment;
    }

    public <T> T replaceFragment(int resid, Class<T> fname, String tag, Bundle bundle) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (tag == null) {
            tag = fname.getName();
        }
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, fname.getName(), bundle);
        }
        ft.replace(resid, fragment, tag);

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        fm.executePendingTransactions();
        return (T) fragment;
    }


}
