c_hideTimeout=500; // 1000==1 second
c_subShowTimeout=300;
c_keepHighlighted=true;
c_findCURRENT=false; // find the item linking to the current page and apply it the CURRENT style class
c_findCURRENTTree=true;
c_overlapControlsInIE=true;
c_rightToLeft=false; // if the menu text should have "rtl" direction (e.g. Hebrew, Arabic)




/******************************************************
	(2) MENU STYLES (CSS CLASSES)
*******************************************************/

// You can define different style classes here and then assign them globally to the menu tree(s)
// in section 3 below or set them to any UL element from your menu tree(s) in the page source


c_imagesPath="dyntags/menuTag/web/"; // path to the directory containing the menu images


c_styles['MM']=[ // MainMenu (the shorter the class name the better)
[
// MENU BOX STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'#8F90C4',	// BorderColor ('color')
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
'#6FADE6',	// OVER BorderColor
6,		// Padding
'#FFFFFF',	// Background ('color','transparent','[image_source]')
'#EFFFE2',	// OVER Background
'#0089C4',	// Color
'#EA6D00',	// OVER Color
'13px',		// FontSize (values in CSS valid units - %,em,ex,px,pt)
'"trebuchet ms",verdana,arial,helvetica,sans-serif',	// FontFamily
'bold',		// FontWeight (CSS valid values - 'bold','normal','bolder','lighter','100',...,'900')
'none',		// TextDecoration (CSS valid values - 'none','underline','overline','line-through')
'none',		// OVER TextDecoration
'left',		// TextAlign ('left','center','right','justify')
0,		// ItemsSeparatorSize
'solid',	// ItemsSeparatorStyle (border-style valid values)
'#8F90C4',	// ItemsSeparatorColor ('color','transparent')
0,		// ItemsSeparatorSpacing
true,			// UseSubMenuImage (true,false)
'[h_web2.0_arrow.gif]',	// SubMenuImageSource ('[image_source]')
'[h_web2.0_arrow.gif]',	// OverSubMenuImageSource
8,			// SubMenuImageWidth
4,			// SubMenuImageHeight
'14',			// SubMenuImageVAlign ('pixels from item top','middle')
'solid',		// VISITED BorderStyle
'#FFFFFF',		// VISITED BorderColor
'#FFFFFF',		// VISITED Background
'#0089C4',		// VISITED Color
'none',			// VISITED TextDecoration
'[h_web2.0_arrow.gif]',	// VISITED SubMenuImageSource
'solid',		// CURRENT BorderStyle
'#FFFFFF',		// CURRENT BorderColor
'#FFFAE6',		// CURRENT Background
'#0089C4',		// CURRENT Color
'none',			// CURRENT TextDecoration
'[h_web2.0_arrow.gif]',	// CURRENT SubMenuImageSource
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
'#F1922F',	// BorderColor ('color')
0,		// Padding
'#FFFFFF',	// Background ('color','transparent','[image_source]')
'',		// IEfilter (only transition filters work well - not static filters)
'border-bottom-width:2px;'	// Custom additional CSS for the menu box (valid CSS)
],[
// MENU ITEMS STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'solid',	// OVER BorderStyle
'#FFFFFF',	// BorderColor ('color')
'#FFFFFF',	// OVER BorderColor
6,		// Padding
'#FFFFFF',	// Background ('color','transparent','[image_source]')
'#EFFFE2',	// OVER Background
'#0089C4',	// Color
'#EA6D00',	// OVER Color
'13px',		// FontSize (values in CSS valid units - %,em,ex,px,pt)
'"trebuchet ms",verdana,arial,helvetica,sans-serif',	// FontFamily
'bold',		// FontWeight (CSS valid values - 'bold','normal','bolder','lighter','100',...,'900')
'none',		// TextDecoration (CSS valid values - 'none','underline','overline','line-through')
'none',		// OVER TextDecoration
'left',		// TextAlign ('left','center','right','justify')
0,		// ItemsSeparatorSize
'solid',	// ItemsSeparatorStyle (border-style valid values)
'#8F90C4',	// ItemsSeparatorColor ('color','transparent')
0,		// ItemsSeparatorSpacing
true,			// UseSubMenuImage (true,false)
'[v_web2.0_arrow.gif]',	// SubMenuImageSource ('[image_source]')
'[v_web2.0_arrow.gif]',	// OverSubMenuImageSource
8,			// SubMenuImageWidth
8,			// SubMenuImageHeight
'11',			// SubMenuImageVAlign ('pixels from item top','middle')
'solid',		// VISITED BorderStyle
'#FFFFFF',		// VISITED BorderColor
'#FFFFFF',		// VISITED Background
'#0089C4',		// VISITED Color
'none',			// VISITED TextDecoration
'[v_web2.0_arrow.gif]',	// VISITED SubMenuImageSource
'solid',		// CURRENT BorderStyle
'#FFFFFF',		// CURRENT BorderColor
'#FFFAE6',		// CURRENT Background
'#0089C4',		// CURRENT Color
'none',			// CURRENT TextDecoration
'[v_web2.0_arrow.gif]',	// CURRENT SubMenuImageSource
'padding-left:8px;padding-right:12px;',		// Custom additional CSS for the items (valid CSS)
'',		// OVER Custom additional CSS for the items (valid CSS)
'',		// CURRENT Custom additional CSS for the items (valid CSS)
''		// VISITED Custom additional CSS for the items (valid CSS)
]];
