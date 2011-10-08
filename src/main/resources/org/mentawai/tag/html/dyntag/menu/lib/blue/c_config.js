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


c_imagesPath="dyntags/menuTag/blue/"; // path to the directory containing the menu images


c_styles['MM']=[ // MainMenu (the shorter the class name the better)
[
// MENU BOX STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'#8F90C4',	// BorderColor ('color')
0,		// Padding
'#F6F6F8',	// Background ('color','transparent','[image_source]')
'',		// IEfilter (only transition filters work well - not static filters)
''		// Custom additional CSS for the menu box (valid CSS)
],[
// MENU ITEMS STYLE
1,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'solid',	// OVER BorderStyle
'#F6F6F8',	// BorderColor ('color')
'#8F90C4',	// OVER BorderColor
5,		// Padding
'#F6F6F8',	// Background ('color','transparent','[image_source]')
'#D9D9E7',	// OVER Background
'#4A49A8',	// Color
'#4A49A8',	// OVER Color
'10px',		// FontSize (values in CSS valid units - %,em,ex,px,pt)
'verdana,arial,helvetica,sans-serif',	// FontFamily
'bold',		// FontWeight (CSS valid values - 'bold','normal','bolder','lighter','100',...,'900')
'none',		// TextDecoration (CSS valid values - 'none','underline','overline','line-through')
'none',		// OVER TextDecoration
'left',		// TextAlign ('left','center','right','justify')
0,		// ItemsSeparatorSize
'solid',	// ItemsSeparatorStyle (border-style valid values)
'#8F90C4',	// ItemsSeparatorColor ('color','transparent')
0,		// ItemsSeparatorSpacing
true,				// UseSubMenuImage (true,false)
'[h_arrow_dark_blue.gif]',	// SubMenuImageSource ('[image_source]')
'[h_arrow_dark_blue.gif]',	// OverSubMenuImageSource
7,				// SubMenuImageWidth
4,				// SubMenuImageHeight
'11',				// SubMenuImageVAlign ('pixels from item top','middle')
'solid',			// VISITED BorderStyle
'#F6F6F8',			// VISITED BorderColor
'#F6F6F8',			// VISITED Background
'#4A49A8',			// VISITED Color
'none',				// VISITED TextDecoration
'[h_arrow_dark_blue.gif]',	// VISITED SubMenuImageSource
'solid',			// CURRENT BorderStyle
'#8F90C4',			// CURRENT BorderColor
'#C5C5EE',			// CURRENT Background
'#4A49A8',			// CURRENT Color
'none',				// CURRENT TextDecoration
'[h_arrow_dark_blue.gif]',	// CURRENT SubMenuImageSource
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
'#8F90C4',	// BorderColor ('color')
0,		// Padding
'#F6F6F8',	// Background ('color','transparent','[image_source]')
'',		// IEfilter (only transition filters work well - not static filters)
''		// Custom additional CSS for the menu box (valid CSS)
],[
// MENU ITEMS STYLE
0,		// BorderWidth
'solid',	// BorderStyle (CSS valid values except 'none')
'solid',	// OVER BorderStyle
'#FFFFFF',	// BorderColor ('color')
'#FFFFFF',	// OVER BorderColor
5,		// Padding
'#F6F6F8',	// Background ('color','transparent','[image_source]')
'#D9D9E7',	// OVER Background
'#4A49A8',	// Color
'#4A49A8',	// OVER Color
'10px',		// FontSize (values in CSS valid units - %,em,ex,px,pt)
'verdana,arial,helvetica,sans-serif',	// FontFamily
'bold',		// FontWeight (CSS valid values - 'bold','normal','bolder','lighter','100',...,'900')
'none',		// TextDecoration (CSS valid values - 'none','underline','overline','line-through')
'none',		// OVER TextDecoration
'left',		// TextAlign ('left','center','right','justify')
1,		// ItemsSeparatorSize
'solid',	// ItemsSeparatorStyle (border-style valid values)
'#8F90C4',	// ItemsSeparatorColor ('color','transparent')
0,		// ItemsSeparatorSpacing
true,				// UseSubMenuImage (true,false)
'[v_arrow_dark_blue.gif]',	// SubMenuImageSource ('[image_source]')
'[v_arrow_dark_blue.gif]',	// OverSubMenuImageSource
7,				// SubMenuImageWidth
7,				// SubMenuImageHeight
'7',				// SubMenuImageVAlign ('pixels from item top','middle')
'solid',			// VISITED BorderStyle
'#FFFFFF',			// VISITED BorderColor
'#F6F6F8',			// VISITED Background
'#4A49A8',			// VISITED Color
'none',				// VISITED TextDecoration
'[v_arrow_dark_blue.gif]',	// VISITED SubMenuImageSource
'solid',			// CURRENT BorderStyle
'#FFFFFF',			// CURRENT BorderColor
'#C5C5EE',			// CURRENT Background
'#4A49A8',			// CURRENT Color
'none',				// CURRENT TextDecoration
'[v_arrow_dark_blue.gif]',	// CURRENT SubMenuImageSource
'',		// Custom additional CSS for the items (valid CSS)
'',		// OVER Custom additional CSS for the items (valid CSS)
'',		// CURRENT Custom additional CSS for the items (valid CSS)
''		// VISITED Custom additional CSS for the items (valid CSS)
]];
