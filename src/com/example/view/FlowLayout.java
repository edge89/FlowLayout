package com.example.view;

import java.util.ArrayList;
import java.util.List;

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
		int SizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int ModeHeight = MeasureSpec.getMode(heightMeasureSpec);
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
			if (i == cCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}

		}

		setMeasuredDimension(ModeWidth == MeasureSpec.EXACTLY ? SizeWidth
				: width, ModeHeight == MeasureSpec.EXACTLY ? SizeHeight
				: height);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 与当前ViewGroup对应的LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {

		return new MarginLayoutParams(getContext(), attrs);
	}

	private List<List<View>> mAllViews = new ArrayList<List<View>>();

	private List<Integer> mLineHeight = new ArrayList<Integer>();

	/**
	 * 设置子View的位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllViews.clear();
		mLineHeight.clear();

		// 拿到当前viewgroup的宽度
		int width = getWidth();

		int lineWidth = 0;
		int lineHeight = 0;

		List<View> lineViews = new ArrayList<View>();

		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			// 如果需要换行
			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
				// 记录lineheight
				mLineHeight.add(lineHeight);
				// 记录当前行的Views
				mAllViews.add(lineViews);
				// 重置行宽和行高
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				// 重置View集合
				lineViews = new ArrayList<View>();

			}
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
		}// for end
			// 处理最后一行
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		// 设置子view的位置
		int left = 0;// 左位置
		int top = 0;// 高度
		int lineNum = mAllViews.size();
		for (int i = 0; i < lineNum; i++) {
			// 当前行所有的view
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				// 判断child的可见状态
				if (child.getVisibility() == View.GONE) {
					continue;
				}

				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				// 为子view进行布局
				child.layout(lc, tc, rc, bc);
				left += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
			}
			left = 0;
			top += lineHeight;
		}
	}
}
