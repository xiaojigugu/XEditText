package com.junt.xedit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class XEditText extends ViewGroup implements View.OnFocusChangeListener {

    /**
     * 输入框是否拥有焦点
     */
    private boolean isFocused;
    private int width, height;
    private final int touchSlop;

    private final TextPaint textPaint;

    /**
     * 左右两侧图片宽度
     */
    private float srcLeftWidth, srcRightWidth;
    /**
     * 左右两侧图片占总宽度的百分比
     */
    private float srcRightBasis, srcLeftBasis;
    /**
     * 左侧图片 - 正常
     */
    private Drawable srcLeftNormalDrawable;
    /**
     * 左侧图片 - 聚焦
     */
    private Drawable srcLeftFocusedDrawable;
    /**
     * 右侧图片 - 正常
     */
    private Drawable srcRightNormalDrawable;
    /**
     * 右侧图片 - 聚焦
     */
    private Drawable srcRightFocusedDrawable;
    /**
     * 右侧图片 - 是否显示
     */
    private boolean isSrcRightShow = false;
    /**
     * 右侧图片 - 显示模式 0.不现实 1.显示 2.获取焦点时显示 3.失去焦点时显示
     */
    private SrcVisibleMode srcRightVisibleMode;
    /**
     * 左侧图片 - 是否显示
     */
    private boolean isSrcLeftShow = false;
    /**
     * 右侧图片 - 显示模式 0.不现实 1.显示 2.获取焦点时显示 3.失去焦点时显示
     */
    private SrcVisibleMode srcLeftVisibleMode;

    /**
     * 描述文字
     */
    private String des_text;
    /**
     * 描述文字 - 矩形范围
     */
    private Rect desRect;
    /**
     * 描述文字 - 颜色
     */
    private int des_text_color_normal, des_text_color_focused;
    /**
     * 描述文字 - 大小
     */
    private int des_text_size;

    /**
     * 输入框 - 初始文字
     */
    private String text;
    /**
     * 输入框 - 大小
     */
    private int textSize;
    /**
     * 输入框 - 颜色
     */
    private int textColor;

    /**
     * 输入框 - 提示文字
     */
    private String hint;
    /**
     * 输入框 - 提示文字颜色
     */
    private int hintColor;

    /**
     * 底部下划线 - 正常
     */
    private Drawable underlineNormalDrawable;
    /**
     * 底部下划线 - 聚焦
     */
    private Drawable underlineFocusedDrawable;
    /**
     * 底部下划线 - 高度
     */
    private int underLineHeight;
    /**
     * ImeOption
     */
    private int imeOptions;
    /**
     * 最大行高（行数 * 行高）
     */
    private int maxLines;
    /**
     * 行数
     */
    private int lines;
    /**
     * 最大字符数
     */
    private int maxLength;
    /**
     * gravity
     */
    private int gravity;
    /**
     * 输入框
     */
    private final EditText editText;
    /**
     * 文字变化监听
     */
    private TextWatcher textWatcher;

    /**
     * 当前点击的图案位置标记
     */
    private SrcLoc srcLoc;

    /**
     * 图案点击监听
     */
    private OnXEditListener onXEditListener;
    private int inputTyp;

    private final InputMethodManager inputManager;

    public XEditText(Context context) {
        this(context, null);
    }

    public XEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);

        handleAttrs(context, attrs);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = new EditText(context);
        editText.setBackground(null);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        editText.setTextColor(textColor);
        editText.setHintTextColor(hintColor);
        editText.setInputType(inputTyp);
        editText.setHint(hint);
        editText.setText(text);
        editText.setGravity(gravity);
        editText.setMaxLines(maxLines);
        if (lines > 0) {
            editText.setLines(lines);
        }
        if (maxLength >= 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        editText.setImeOptions(imeOptions);
        addView(editText);
    }

    private void handleAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XEditText);

        String s = typedArray.getString(R.styleable.XEditText_XE_description);
        des_text = TextUtils.isEmpty(s) ? "" : s;
        des_text_color_normal = typedArray.getColor(R.styleable.XEditText_XE_description_text_Color_normal, Color.LTGRAY);
        des_text_color_focused = typedArray.getColor(R.styleable.XEditText_XE_description_text_Color_focused, des_text_color_normal);
        des_text_size = typedArray.getDimensionPixelSize(R.styleable.XEditText_XE_description_text_Size, (int) applyDimension(context, TypedValue.COMPLEX_UNIT_SP, 14));

        textPaint.setTextSize(des_text_size);
        desRect = new Rect(0, 0, 0, 0);
        if (!TextUtils.isEmpty(des_text)) {
            textPaint.getTextBounds("我", 0, 1, desRect);
        }

        srcLeftNormalDrawable = typedArray.getDrawable(R.styleable.XEditText_XE_srcLeft_normal);
        srcLeftFocusedDrawable = typedArray.getDrawable(R.styleable.XEditText_XE_srcLeft_focused);
        srcRightNormalDrawable = typedArray.getDrawable(R.styleable.XEditText_XE_srcRight_normal);
        srcRightFocusedDrawable = typedArray.getDrawable(R.styleable.XEditText_XE_srcRight_focused);
        srcRightVisibleMode = SrcVisibleMode.values()[typedArray.getInt(R.styleable.XEditText_XE_srcRight_visible, 1)];
        isSrcRightShow = (srcRightVisibleMode == SrcVisibleMode.VISIBLE || srcRightVisibleMode == SrcVisibleMode.UNFOCUSED);
        srcLeftVisibleMode = SrcVisibleMode.values()[typedArray.getInt(R.styleable.XEditText_XE_srcLeft_visible, 1)];
        isSrcLeftShow = (srcLeftVisibleMode == SrcVisibleMode.VISIBLE || srcLeftVisibleMode == SrcVisibleMode.UNFOCUSED);

        srcLeftBasis = typedArray.getFloat(R.styleable.XEditText_XE_srcLeftBasis, isSrcLeftExist(srcLeftNormalDrawable, srcLeftFocusedDrawable) ? 0.05f : 0);
        srcRightBasis = typedArray.getFloat(R.styleable.XEditText_XE_srcRightBasis, isSrcLeftExist(srcRightNormalDrawable, srcRightFocusedDrawable) ? 0.05f : 0);

        s = typedArray.getString(R.styleable.XEditText_XE_text);
        text = TextUtils.isEmpty(s) ? "" : s;
        textSize = typedArray.getDimensionPixelSize(R.styleable.XEditText_XE_textSize, (int) applyDimension(context, TypedValue.COMPLEX_UNIT_SP, 14));
        textColor = typedArray.getColor(R.styleable.XEditText_XE_textColor, Color.BLACK);

        hint = typedArray.getString(R.styleable.XEditText_XE_hint);
        hintColor = typedArray.getColor(R.styleable.XEditText_XE_hintTextColor, Color.GRAY);

        underLineHeight = typedArray.getDimensionPixelSize(R.styleable.XEditText_XE_underLine_height, (int) applyDimension(context, TypedValue.COMPLEX_UNIT_DIP, 1));
        underlineNormalDrawable = typedArray.getDrawable(R.styleable.XEditText_XE_underLine_normal);
        underlineFocusedDrawable = typedArray.getDrawable(R.styleable.XEditText_XE_underLine_focused);

        gravity = typedArray.getInt(R.styleable.XEditText_XE_gravity, Gravity.CENTER_VERTICAL);
        inputTyp = typedArray.getInt(R.styleable.XEditText_XE_inputType, InputType.TYPE_NULL);
        maxLines = typedArray.getInt(R.styleable.XEditText_XE_maxLines, 1);
        lines = typedArray.getInt(R.styleable.XEditText_XE_lines, -1);
        maxLength = typedArray.getInt(R.styleable.XEditText_XE_maxLength, -1);
        imeOptions = typedArray.getInt(R.styleable.XEditText_XE_imeOptions, EditorInfo.IME_NULL);

        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        srcLeftWidth = w * srcLeftBasis;
        srcRightWidth = w * srcRightBasis;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        int top = getPaddingTop() + desRect.height();
        int bottom = height - getPaddingBottom() - underLineHeight;
        if (bottom - top < srcRightWidth) {
            srcRightWidth = bottom - top;
        }
        if (bottom - top < srcLeftWidth) {
            srcLeftWidth = bottom - top;
        }

        int left = (int) (getPaddingLeft() + (isSrcLeftExist(srcLeftNormalDrawable, srcLeftFocusedDrawable) ? srcLeftWidth : 0));
        int right = (int) (width - getPaddingRight() - (isSrcLeftExist(srcRightNormalDrawable, srcRightFocusedDrawable) ? srcRightWidth : 0));

        child.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDes(canvas);
        drawSrc(canvas);
        drawUnderLine(canvas);
    }

    /**
     * 绘制下划线
     *
     * @param canvas canvas
     */
    private void drawUnderLine(Canvas canvas) {
        Drawable underlineDrawable = null;
        if (underlineNormalDrawable != null) {
            underlineDrawable = isFocused ? underlineFocusedDrawable : underlineNormalDrawable;
        }
        if (underlineDrawable != null) {
            underlineDrawable.setBounds(getPaddingLeft(), height - getPaddingBottom() - underLineHeight, width - getPaddingRight(), height - getPaddingBottom());
            underlineDrawable.draw(canvas);
        }
    }

    /**
     * 绘制两侧图案
     *
     * @param canvas canvas
     */
    private void drawSrc(Canvas canvas) {
        Drawable srcLeftDrawable = null, srcRightDrawable = null;

        if (isSrcLeftShow && srcLeftNormalDrawable != null) {
            srcLeftDrawable = isFocused ? (srcLeftFocusedDrawable == null ? srcLeftNormalDrawable : srcLeftFocusedDrawable) : srcLeftNormalDrawable;
        }

        if (isSrcRightShow && srcRightNormalDrawable != null) {
            srcRightDrawable = isFocused ? (srcRightFocusedDrawable == null ? srcRightNormalDrawable : srcRightFocusedDrawable) : srcRightNormalDrawable;
        }

        int centerEditTextY = editText.getTop() + editText.getHeight() / 2;
        if (srcLeftDrawable != null) {
            srcLeftDrawable.setBounds(getPaddingLeft(), (int) (centerEditTextY - srcLeftWidth / 2), editText.getLeft(), (int) (centerEditTextY + srcLeftWidth / 2));
            srcLeftDrawable.draw(canvas);
        }
        if (srcRightDrawable != null) {
            srcRightDrawable.setBounds(editText.getRight(), (int) (centerEditTextY - srcRightWidth / 2), width - getPaddingRight(), (int) (centerEditTextY + srcRightWidth / 2));
            srcRightDrawable.draw(canvas);
        }
    }

    private boolean isSrcLeftExist(Drawable srcFocused, Drawable srcNormal) {
        return srcFocused != null || srcNormal != null;
    }

    /**
     * 绘制描述文字
     *
     * @param canvas canvas
     */
    private void drawDes(Canvas canvas) {
        textPaint.setTextSize(des_text_size);
        textPaint.setColor(isFocused ? des_text_color_focused : des_text_color_normal);
        textPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(des_text, getPaddingLeft(), getPaddingTop() + desRect.height(), textPaint);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public void setOnXEditListener(OnXEditListener onXEditListener) {
        this.onXEditListener = onXEditListener;
    }

    public EditText getEditText() {
        return editText;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (srcLeftNormalDrawable != null && ev.getX() < editText.getLeft()) {
            srcLoc = SrcLoc.LEFT;
            return true;
        }
        if (srcRightNormalDrawable != null && ev.getX() > editText.getRight()) {
            srcLoc = SrcLoc.RIGHT;
            return true;
        }
        if (ev.getX() >= editText.getLeft() && ev.getX() <= editText.getRight()) {
            srcLoc = SrcLoc.INPUT;
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    private float downX;
    private float downY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                double hypot = Math.hypot(x - downX, y - downY);
                if (hypot <= touchSlop) {
                    if (srcLoc == SrcLoc.INPUT) {
                        editText.requestFocus();
                        inputManager.showSoftInput(editText, 0);
                    }
                    if (onXEditListener != null) {
                        onXEditListener.onSrcClick(XEditText.this, srcLoc);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (editText != null) {
            if (textWatcher != null) {
                editText.addTextChangedListener(textWatcher);
            }
            editText.setOnFocusChangeListener(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (editText != null) {
            if (textWatcher != null) {
                editText.setOnFocusChangeListener(null);
            }
            editText.removeTextChangedListener(textWatcher);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int widthSpec = MeasureSpec.makeMeasureSpec((int) (width - (isSrcLeftShow ? srcLeftWidth : 0) - (isSrcRightShow ? srcRightWidth : 0)), MeasureSpec.EXACTLY);

        editText.measure(widthSpec, heightSpec);
        setMeasuredDimension(width, (int) (desRect.height() + editText.getMeasuredHeight() + underLineHeight + getPaddingTop() + getPaddingBottom()));
    }

    private float applyDimension(Context context, int unit, int value) {
        Resources resources = context.getResources();
        return TypedValue.applyDimension(unit, value, resources.getDisplayMetrics());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        isFocused = hasFocus;
        if (srcRightVisibleMode == SrcVisibleMode.FOCUSED) {
            isSrcRightShow = hasFocus;

        } else if (srcRightVisibleMode == SrcVisibleMode.UNFOCUSED) {
            isSrcRightShow = !hasFocus;
        }
        if (srcLeftVisibleMode == SrcVisibleMode.FOCUSED) {
            isSrcLeftShow = hasFocus;
        } else if (srcLeftVisibleMode == SrcVisibleMode.UNFOCUSED) {
            isSrcLeftShow = !hasFocus;
        }
        if (onXEditListener != null) {
            onXEditListener.onFocusChange(XEditText.this, hasFocus);
        }
        invalidate();
    }

    public interface OnXEditListener {
        void onSrcClick(XEditText xEditText, SrcLoc srcLoc);

        void onFocusChange(XEditText xEditText, boolean hasFocus);
    }

    public enum SrcLoc {
        LEFT,
        RIGHT,
        INPUT
    }

    /**
     * 右侧图片显示模式
     */
    enum SrcVisibleMode {
        INVISIBLE,
        VISIBLE,
        FOCUSED,
        UNFOCUSED
    }
}
