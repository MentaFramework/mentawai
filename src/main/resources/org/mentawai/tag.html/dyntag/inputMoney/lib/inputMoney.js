/**************************************
* Classe:MaskInput                    *
* Jonas Raoni Soares Silva            *
* http://www.joninhas.ath.cx          *
**************************************/
/**************************************
* 20/03/2006-Alterada por Alex Fortuna*
* fortunao@hotmail.com                *
**************************************/
inputMoneyAddEvent = function(o, e, f, s){
    var r = o[r = "_" + (e = "on" + e)] = o[r] || (o[e] ? [[o[e], o]] : []), a, c, d;
    r[r.length] = [f, s || o], o[e] = function(e){
        try{
            (e = e || event).preventDefault || (e.preventDefault = function(){e.returnValue = false;});
            e.stopPropagation || (e.stopPropagation = function(){e.cancelBubble = true;});
            e.target || (e.target = e.srcElement || null);
            e.key = (e.which + 1 || e.keyCode + 1) - 1 || 0;
        }catch(f){}
        for(d = 1, f = r.length; f; r[--f] && (a = r[f][0], o = r[f][1], a.call ? c = a.call(o, e) : (o._ = a, c = o._(e), o._ = null), d &= c !== false));
        return e = null, !!d;
    }
};

inputMoneyMask = function(o, n, dig, dec){
    o.c = !isNaN(n) ? Math.abs(n) : 2;
    o.dec = dec || ",", o.dig = dig || ".";
    inputMoneyAddEvent(o, "keypress", function(e){
        if(e.key > 47 && e.key < 58){
            var o, s = ((o = this).value.replace(/^0+/g, "") + String.fromCharCode(e.key)).replace(/\D/g, ""), l, n;
            (l = s.length) <= (n = o.c) && (s = new Array(n - l + 2).join("0") + s);
            for(var i = (l = (s = s.split("")).length) - n; (i -= 3) > 0; s[i - 1] += o.dig);
            n && n < l && (s[l - ++n] += o.dec);
            o.value = s.join("");
        }
        e.key > 30 && e.preventDefault();
    });
}