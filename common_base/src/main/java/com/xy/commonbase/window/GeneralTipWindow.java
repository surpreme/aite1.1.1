package com.xy.commonbase.window;

import com.xy.commonbase.R;
import com.xy.commonbase.base.BaseTipDialogFragment;
import com.xy.commonbase.constants.Constants;

public class GeneralTipWindow<T> extends BaseTipDialogFragment<T> {

    @Override
    protected CharSequence getTitle() {
        return mContext.getString(R.string.base_tips_title);
    }

    @Override
    protected CharSequence getContent() {
        return getArguments().getString(Constants.ARG_PARAM1);
    }

    @Override
    protected CharSequence getRight() {
        return mContext.getString(R.string.confirm);
    }

    @Override
    protected CharSequence getLeft() {
        return mContext.getString(R.string.cancel);
    }
}
