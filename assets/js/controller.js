// Bindings go here


$(document).ready(function(){
	var game = new Game();
	game.beginGame();

    function tipsWordGetter() {
        this.words = ["<p><font color='#99FFFF'>如果您喜欢这个应用请点击</font><b><big>安装</big></b><font color='#99FFFF'>，谢谢您>的支持。<br>如果不喜欢您可以点击右上角的关闭按钮或者返回键。</font></p><p>广告很多？想要获得无广告版本。请邮件联系:hanxi.info@gmai.com</p>",
];
        this.getWord = function() {
            return this.words[Math.floor(Math.random()*this.words.length)];
        }
    }

	$('.reset').click(function(){
		$('#restartLevel').modal('show');
        twg = new tipsWordGetter();
        window.android.showAdsFull(twg.getWord());
	});

	$('.newgame').click(function(){
		$('#newGame').modal('show');
        twg = new tipsWordGetter();
        window.android.showAdsFull(twg.getWord());
	});

	$('#resetLevelConfirm').click(function(){
		game.onResetLevelClick();
	});

	$('#newGameConfirm').click(function(){
		game.onNewGameClick();
	});

	$('.instruct').click(function(){
		$('#instructions').modal('show');
	})
    
	$('.share').click(function(){
        window.android.share('Inverter中文版拼图','Inverter中文版拼图-快来玩吧：http://www.wandoujia.com/apps/com.hanxi.inverter ','给好友分享');
	})

	$('.showAdsFull').click(function(){
        twg = new tipsWordGetter();
        window.android.showAdsFull(twg.getWord());
	})

	$('.showAdsAppWall').click(function(){
        window.android.showAdsAppWall();
	})
});
