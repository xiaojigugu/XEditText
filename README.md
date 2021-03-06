# XEditText

[![](https://jitpack.io/v/xiaojigugu/XEditText.svg)](https://jitpack.io/#xiaojigugu/XEditText)

    An EditText library with multi funtions which is based on native EditText.
    You can set description、drawable(left or right)and underline.

![XEditText](https://s1.ax1x.com/2020/07/28/aAyPvn.png)

#### USE:

```groovy
//Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
//Add the dependency
	dependencies {
	        implementation 'com.github.xiaojigugu:XEditText:1.0'
	}
```



```xml
   <com.junt.xedit.XEditText
        android:id="@+id/xEditText1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        app:XE_description="@string/app_name"
        app:XE_description_text_Color_focused="@color/colorPrimaryDark"
        app:XE_description_text_Color_normal="@color/colorPrimary"
        app:XE_description_text_Size="18sp"
        app:XE_gravity="bottom|end"
        app:XE_hint="请输入"
        app:XE_hintTextColor="#716F6F"
        app:XE_imeOptions="actionSearch"
        app:XE_inputType="textMultiLine"
        app:XE_lines="2"
        app:XE_maxLines="3"
        app:XE_srcLeftBasis="0.15"
        app:XE_srcLeft_focused="@mipmap/ic_launcher_round"
        app:XE_srcLeft_normal="@mipmap/ic_launcher"
        app:XE_srcLeft_visible="VISIBLE"
        app:XE_srcRightBasis="0.05"
        app:XE_srcRight_focused="@color/colorPrimaryDark"
        app:XE_srcRight_normal="@color/colorPrimary"
        app:XE_srcRight_visible="VISIBLE"
        app:XE_text=""
        app:XE_textColor="@color/colorAccent"
        app:XE_textSize="20sp"
        app:XE_underLine_focused="@mipmap/ic_launcher_round"
        app:XE_underLine_height="2dp"
        app:XE_underLine_normal="@color/colorAccent" />
```

```java
        XEditText xEditText=findViewById(R.id.xEditText1);
        
        xEditText.setOnXEditListener(new XEditText.OnXEditListener() {
            @Override
            public void onSrcClick(XEditText xEditText, XEditText.SrcLoc srcLoc) {
                //when drawable is clicked
                if (srcLoc== XEditText.SrcLoc.LEFT){
                    //do sth.
                }else {
                    //do sth.
                }
            }

            @Override
            public void onFocusChange(XEditText xEditText, boolean hasFocus) {
                if (hasFocus){
                    //do sth.
                }else {
                    //do sth.
                }
            }
        });
        
        xEditText.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        //get text
        String s=xEditText.getText();
        
        //do more configuration
        EditText editText = xEditText.getEditText();
        editText.requestFocus();
```



#### API：

![split](https://s1.ax1x.com/2020/07/28/aA2MFK.png)

1. description
2. Area of EditText
3. srcLeft
4. srcRight
5. underline

| XE_description                    |                                                              |
| :-------------------------------- | ------------------------------------------------------------ |
| XE_description_text_Size          | default: 14sp                                                |
| XE_description_text_Color_normal  | default: Color.LTGRAY                                        |
| XE_description_text_Color_focused | default: des_text_color_normal                               |
| XE_srcLeft_normal                 | accept  xml/ img/color                                       |
| XE_srcLeft_focused                | accept  xml/ img/color                                       |
| XE_srcRight_normal                | accept  xml/ img/color                                       |
| XE_srcRight_focused               | accept  xml/ img/color                                       |
| XE_srcRight_visible               | INVISIBLE、VISIBLE、FOCUSED、UNFOCUSED                       |
| XE_srcLeft_visible                | INVISIBLE、VISIBLE、FOCUSED、UNFOCUSED                       |
| XE_srcLeftBasis                   | the percent of srcleft's width relative to total width，will not greater than the height of EditText |
| XE_srcRightBasis                  | the percent of srcleft's width relative to total width，will not greater than the height of EditText |
| XE_text                           | default text shown in EditText                               |
| XE_textSize                       | TextSize of EditText，default: 14sp                          |
| XE_textColor                      | TextColor of EditText, default: Color.BLACK                  |
| XE_hint                           | hint of EditText                                             |
| XE_hintTextColor                  | hintTextColor of EditText, default: Color.GRAY               |
| XE_underLine_height               | height of underline, default: 1dp                            |
| XE_underLine_normal               | accept  xml/ img/color                                       |
| XE_underLine_focused              | accept  xml/ img/color                                       |
| XE_gravity                        | gravity of EditText, default: Gravity.CENTER_VERTICAL        |
| XE_inputType                      | inputType  of EditText, default: none                        |
| XE_maxLines                       | max height of EditText, default: 1                           |
| XE_lines                          | height of EditText, default: no limit                        |
| XE_maxLength                      | maxLength of EditText, default: no limit                     |
| XE_imeOptions                     | imeOptions of EditText,default: normal                       |

