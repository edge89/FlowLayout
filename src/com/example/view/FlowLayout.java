package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int SizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int ModeWidth = MeasureSpec.getMode(widthMeasureSpec);
		// int SizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		// int ModeHeight = MeasureSpec.getMode(heightMeasureSpec);
		// 如果 写的是match_parent或者精确值，就可以得到准确的宽高值

		// 如果是 wrap_content view的宽和高需要自己去设置;
		int width = 0;
		int height = 0;

		// 记录每一行的宽度和高度
		int lineWidth = 0;
		int lineHeight = 0;

		// 获取内部元素的个数
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			// 测量子View的宽和高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 得到ViewGroup的LayoutParams 因为只设置了MarginLayoutParams，返回值类型为MarginLayoutParams
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// 获取子View占据的宽度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;

			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			if (lineWidth + childWidth > SizeWidth) {
				// 对比得到最大的宽度
				width = Math.max(lineWidth, width);
				lineWidth = childWidth;
				height += lineHeight;
				lineHeight = childHeight;
			} else {
				// 叠加行宽
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}

			// 最后一个控件
			if (i == cCount) {
				width = Math.max(lineWidth, childWidth);
				height += lineHeight;
			}

		}

		if (ModeWidth == MeasureSpec.AT_MOST) {

		} else {

		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 与当前ViewGroup对应的LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {

		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

}
