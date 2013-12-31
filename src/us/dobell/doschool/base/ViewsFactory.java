package us.dobell.doschool.base;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.MenuGroups.MenuGroup;
import us.dobell.doschool.base.MenuGroups.MenuItem;
import us.dobell.doschool.microblog.At;
import us.dobell.doschool.microblog.Comment;
import us.dobell.doschool.microblog.Microblog;
import us.dobell.doschool.microblog.PageComment;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XUtils;
import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewsFactory {

	public static class BaseFactory {
		public static View getMenuGroupView(Context context, MenuItem menuItem) {
			View itemView = null;
			if (menuItem instanceof MenuGroup) {
				itemView = LayoutInflater.from(context).inflate(
						R.layout.item_menu, null);
				ImageView itemName = (ImageView) itemView
						.findViewById(R.id.itemMenuName);
				if (menuItem.itemName.equals("我的微博")) {
					itemName.setImageResource(R.drawable.item_microblog);
				} else if (menuItem.itemName.equals("我的圈子")) {
					itemName.setImageResource(R.drawable.item_circle);
				} else {
					itemName.setImageResource(R.drawable.item_tools);
				}
				if (menuItem.isSelected) {
					itemView.setBackgroundColor(Color.LTGRAY);
				} else {
					itemView.setBackgroundColor(Color.WHITE);
				}
			} else {
				itemView = LayoutInflater.from(context).inflate(
						R.layout.subitem_menu, null);
				if (menuItem.isSelected) {
					itemView.setBackgroundColor(Color.GRAY);
				} else {
					itemView.setBackgroundColor(Color.WHITE);
				}
				TextView itemName = (TextView) itemView
						.findViewById(R.id.itemMenuName);
				itemName.setText(menuItem.itemName);
			}
			TextView itemNews = (TextView) itemView
					.findViewById(R.id.itemMenuNews);
			if (menuItem.itemNews > 0) {
				itemNews.setVisibility(View.VISIBLE);
				itemNews.setText("" + menuItem.itemNews);
			} else {
				itemNews.setVisibility(View.GONE);
				itemNews.setText("");
			}
			return itemView;
		}
	}

	public static class MicroblogFactory {

		public static View getAtView(Context context, At at) {
			View atView = LayoutInflater.from(context).inflate(
					R.layout.item_at, null);
			((ViewGroup) atView)
					.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			((XImageView) atView.findViewById(R.id.itemAtUserHeadXImg))
					.setImageURL(at.usrHead);
			((TextView) atView.findViewById(R.id.itemAtUserNickTxt))
					.setText(at.usrNick);
			((TextView) atView.findViewById(R.id.itemAtTimeTxt)).setText(XUtils
					.getTime(at.time));
			String title = "";
			switch (at.type) {
			case At.TYPE_MICROBLOG_AT:
				title = "在微博中@了你";
				break;
			case At.TYPE_MICROBLOG_TRANSMIT:
				title = "转发了你的微博";
				break;
			case At.TYPE_MICROBLOG_DELETE:
				title = "删除了你的微博";
				break;
			case At.TYPE_COMMENT_AT:
				title = "在评论中@了你";
				break;
			case At.TYPE_COMMENT_COMMENT:
				title = "回复了你";
				break;
			case At.TYPE_COMMENT_DELETE:
				title = "删除了你的评论";
				break;
			default:
				break;
			}
			((TextView) atView.findViewById(R.id.itemAtTitleTxt))
					.setText(title);
			TextView newContentTxt = (TextView) atView
					.findViewById(R.id.itemAtNewContentTxt);
			newContentTxt.setMovementMethod(LinkMovementMethod.getInstance());
			newContentTxt.setText(at.newContent.toSpannableString());
			TextView oldContentTxt = (TextView) atView
					.findViewById(R.id.itemAtOldContentTxt);
			oldContentTxt.setMovementMethod(LinkMovementMethod.getInstance());
			oldContentTxt.setText(at.oldContent.toSpannableString());
			return atView;
		}

		public static View getMicroblogCommentView(final Context context,
				final Comment comment) {
			View commentView = LayoutInflater.from(context).inflate(
					R.layout.item_comment, null);
			((XImageView) commentView
					.findViewById(R.id.itemCommentUserHeadXImg))
					.setImageURL(comment.usrHead);
			((TextView) commentView.findViewById(R.id.itemCommentUserNickTxt))
					.setText(comment.usrNick);
			((TextView) commentView.findViewById(R.id.itemCommentTimeTxt))
					.setText(XUtils.getTime(comment.time));
			TextView contentTxt = (TextView) commentView
					.findViewById(R.id.itemCommentContentTxt);
			contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
			contentTxt.setText(comment.content.toSpannableString());
			Button btnComment = (Button) commentView
					.findViewById(R.id.itemCommentCommentBtn);
			btnComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						PageComment page = new PageComment(context);
						JSONObject jObj = new JSONObject();
						jObj.put("objId", comment.usrId);
						jObj.put("rootCommentId", comment.id);
						jObj.put("rootMblogId", comment.rootMblogId);
						page.setInfo(jObj);
						ActivityMain.addXPage(page);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			return commentView;
		}

		public static View getMicroblogView(Context context, Microblog mblog) {
			View mblogView = LayoutInflater.from(context).inflate(
					R.layout.view_microblog_normal, null);
			((ViewGroup) mblogView)
					.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			OnClickListener listener = ListenersFactory.MicroblogFactory
					.getMicroblogListener(mblog);
			LinearLayout imagesLayout = (LinearLayout) mblogView
					.findViewById(R.id.viewNormalMicroblogImagesLayout);
			if (mblog.images != null && mblog.images.size() != 0) {
				for (int i = 0; i < mblog.images.size(); i++) {
					XImageView ximg = new XImageView(context);
					LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					l.weight = 1;
					ximg.setImageURL(mblog.images.get(i));
					ximg.setScaleType(ScaleType.FIT_CENTER);
					ximg.setAdjustViewBounds(true);
					ximg.setLayoutParams(l);
					imagesLayout.addView(ximg);
				}
			} else {
				imagesLayout.setVisibility(View.GONE);
			}
			XImageView userHeadXImg = (XImageView) mblogView
					.findViewById(R.id.viewNormalMicroblogUserHeadXImg);
			userHeadXImg.setOnClickListener(listener);
			userHeadXImg.setImageURL(mblog.usrHead);
			TextView userNickTxt = ((TextView) mblogView
					.findViewById(R.id.viewNormalMicroblogUserNickTxt));
			userNickTxt.setOnClickListener(listener);
			userNickTxt.setText(mblog.usrNick);
			TextView timeTxt = (TextView) mblogView
					.findViewById(R.id.viewNormalMicroblogTimeTxt);
			timeTxt.setText(XUtils.getTime(mblog.time));
			TextView contentTxt = ((TextView) mblogView
					.findViewById(R.id.viewNormalMicroblogContentTxt));
			contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
			contentTxt.setText(mblog.content.toSpannableString());
			Button commentBtn = (Button) mblogView
					.findViewById(R.id.viewNormalMicroblogCommentBtn);
			commentBtn.setOnClickListener(listener);
			Button transmitBtn = (Button) mblogView
					.findViewById(R.id.viewNormalMicroblogTransmitBtn);
			transmitBtn.setOnClickListener(listener);
			commentBtn.setText("评论(" + mblog.comment + ")");
			transmitBtn.setText("转发(" + mblog.transmit + ")");
			LinearLayout rootLayout = (LinearLayout) mblogView
					.findViewById(R.id.viewNormalMicroblogRootLayout);
			if (mblog.root != 0) {
				View rootMblogView = LayoutInflater.from(context).inflate(
						R.layout.view_microblog_root, null);
				rootLayout.addView(rootMblogView);
				ViewsFactoryLazy.MicroblogFactory.getRootMicroblogView(
						rootMblogView, mblog.root);
			} else {
				rootLayout.setVisibility(View.GONE);
			}
			return mblogView;
		}
	}
}
