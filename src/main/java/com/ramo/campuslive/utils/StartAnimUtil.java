package com.ramo.campuslive.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class StartAnimUtil {
	private static int i = 1;

	/*
	 * �󵭳�����
	 */
	@SuppressLint("NewApi")
	public static void startAnim(View view, int j) {

		if (1 == j)
			i = 1;

		ValueAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
		animator1.setDuration(1000);
		animator1.setInterpolator(new AccelerateInterpolator());

		ValueAnimator animator2 = ObjectAnimator.ofFloat(view, "x", view.getX(), (view.getX() - view.getWidth() * 2));// �����ƶ�Ч��
		animator2.setDuration(800);
		animator2.setInterpolator(new DecelerateInterpolator());

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setStartDelay(i * 300);
		animatorSet.playTogether(animator1, animator2);
		animatorSet.start();
		i++;
	}

	/*
	 * ��ҳlogo��̬����Ч��
	 */
	public static void firstViewAnim(View view) {
		ValueAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
		animator1.setDuration(1000);
		animator1.setInterpolator(new AccelerateInterpolator());

		ValueAnimator animator2 = ObjectAnimator.ofFloat(view, "y", -500, 500);
		animator2.setDuration(1000);
		animator2.setInterpolator(new DecelerateInterpolator());
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setStartDelay(i * 300);
		animatorSet.playTogether(animator1, animator2);
		animatorSet.start();
	}
}
