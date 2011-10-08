c_hideTimeout=500; // 1000==1 second
c_subShowTimeout=300;
c_keepHighlighted=true;
c_findCURRENT=true; // find the item linking to the current page and apply it the CURRENT style class
c_findCURRENTTree=true;
c_overlapControlsInIE=true;
c_rightToLeft=false; // if the menu text should have "rtl" direction (e.g. Hebrew, Arabic)




/******************************************************
	(2) MENU STYLES (CSS CLASSES)
*******************************************************/

// You can define different style classes here and then assign them globally to the menu tree(s)
// in section 3 below or set them to any UL element from your menu tree(s) in the page source


c_imagesPath="dyntags/menuTag/winxp/"; // path to the directory containing the menu images


c_styles['MM']=[ // MainMenu (the shorter the class name the better)
[
// MENU BOX STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'#55A1FF',	// BorderColor ('color')
0,		// Padding
'#FFFFFF',	// Background ('color','transparent','[image_source]')
'',		// IEfilter (only transition filters work well - not static filters)
''		// Custom additional CSS for the menu box (valid CSS)
],[
// MENU ITEMS STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'solid',	// OVER BorderStyle
'#FFFFFF',	// BorderColor ('color')
'#FFFFFF',	// OVER BorderColor
4,		// Padding
'#FFFFFF',	// Background ('color','transparent','[image_source]')
'#1665CB',	// OVER Background
'#000000',	// Color
'#FFFFFF',	// OVER Color
'0.916em',		// FontSize (values in CSS valid units - %,em,ex,px,pt)
'tahoma,verdana,arial,helvetica,sans-serif',	// FontFamily
'normal',	// FontWeight (CSS valid values - 'bold','normal','bolder','lighter','100',...,'900')
'none',		// TextDecoration (CSS valid values - 'none','underline','overline','line-through')
'none',		// OVER TextDecoration
'left',		// TextAlign ('left','center','right','justify')
0,		// ItemsSeparatorSize
'solid',	// ItemsSeparatorStyle (border-style valid values)
'#999999',	// ItemsSeparatorColor ('color','transparent')
0,		// ItemsSeparatorSpacing
true,			// UseSubMenuImage (true,false)
'[h_arrow_black.gif]',	// SubMenuImageSource ('[image_source]')
'[h_arrow_white.gif]',	// OverSubMenuImageSource
7,			// SubMenuImageWidth
4,			// SubMenuImageHeight
'9',			// SubMenuImageVAlign ('pixels from item top','middle')
'solid',		// VISITED BorderStyle
'#FFFFFF',		// VISITED BorderColor
'#FFFFFF',		// VISITED Background
'#000000',		// VISITED Color
'none',			// VISITED TextDecoration
'[h_arrow_black.gif]',	// VISITED SubMenuImageSource
'solid',		// CURRENT BorderStyle
'#F1F1F1',		// CURRENT BorderColor
'#FEEAB6',		// CURRENT Background
'#000000',		// CURRENT Color
'none',			// CURRENT TextDecoration
'[h_arrow_black.gif]',	// CURRENT SubMenuImageSource
'',		// Custom additional CSS for the items (valid CSS)
'',		// OVER Custom additional CSS for the items (valid CSS)
'',		// CURRENT Custom additional CSS for the items (valid CSS)
''		// VISITED Custom additional CSS for the items (valid CSS)
]];


c_styles['SM']=[ // SubMenus
[
// MENU BOX STYLE
1,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'#55A1FF',	// BorderColor ('color')
3,		// Padding
'[winxp_menu_bg.gif]',	// Background ('color','transparent','[image_source]')
'',		// IEfilter (only transition filters work well - not static filters)
'padding:0;padding-left:5px;'		// Custom additional CSS for the menu box (valid CSS)
],[
// MENU ITEMS STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'solid',	// OVER BorderStyle
'#FFFFFF',	// BorderColor ('color')
'#FFFFFF',	// OVER BorderColor
4,		// Padding
'#FFFFFF',	// Background ('color','transparent','[image_source]')
'#1665CB',	// OVER Background
'#000000',	// Color
'#FFFFFF',	// OVER Color
'0.916em',		// FontSize (values in CSS valid units - %,em,ex,px,pt)
'tahoma,verdana,arial,helvetica,sans-serif',	// FontFamily
'normal',	// FontWeight (CSS valid values - 'bold','normal','bolder','lighter','100',...,'900')
'none',		// TextDecoration (CSS valid values - 'none','underline','overline','line-through')
'none',		// OVER TextDecoration
'left',		// TextAlign ('left','center','right','justify')
0,		// ItemsSeparatorSize
'solid',	// ItemsSeparatorStyle (border-style valid values)
'#999999',	// ItemsSeparatorColor ('color','transparent')
0,		// ItemsSeparatorSpacing
true,			// UseSubMenuImage (true,false)
'[v_arrow_black.gif]',	// SubMenuImageSource ('[image_source]')
'[v_arrow_white.gif]',	// OverSubMenuImageSource
7,			// SubMenuImageWidth
7,			// SubMenuImageHeight
'6',			// SubMenuImageVAlign ('pixels from item top','middle')
'solid',		// VISITED BorderStyle
'#FFFFFF',		// VISITED BorderColor
'#FFFFFF',		// VISITED Background
'#000000',		// VISITED Color
'none',			// VISITED TextDecoration
'[v_arrow_black.gif]',	// VISITED SubMenuImageSource
'solid',		// CURRENT BorderStyle
'#FFFFFF',		// CURRENT BorderColor
'#FEEAB6',		// CURRENT Background
'#000000',		// CURRENT Color
'none',			// CURRENT TextDecoration
'[v_arrow_black.gif]',	// CURRENT SubMenuImageSource
'padding-left:5px;',	// Custom additional CSS for the items (valid CSS)
'',		// OVER Custom additional CSS for the items (valid CSS)
'',		// CURRENT Custom additional CSS for the items (valid CSS)
''		// VISITED Custom additional CSS for the items (valid CSS)
]];
