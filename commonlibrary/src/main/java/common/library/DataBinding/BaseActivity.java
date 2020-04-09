package common.library.DataBinding;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity <VM extends AndroidViewModel, SV extends ViewDataBinding> extends AppCompatActivity {

    // ViewModel
    protected VM viewModel;
    // 布局view
    protected SV bindingView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        initData();
        //initViewModel();
        //bindingView = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);


    /**
     * 初始化ViewModel
     */
    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass != null) {
            this.viewModel = ViewModelProviders.of(this).get(viewModelClass);
            //页面数据初始化方法
            initData();
        }
    }

    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBus(Object command){
        initEventBus(command);
    }

    protected abstract void initEventBus(Object command);
}
