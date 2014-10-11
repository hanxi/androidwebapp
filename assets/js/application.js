animationDelay = 100;
minSearchTime = 50;

// Wait till the browser is ready to render the game (avoids glitches)
window.requestAnimationFrame(function () {
  var manager = new GameManager(4, KeyboardInputManager, HTMLActuator);

  document.getElementById('share').onclick = function(){
      window.android.share('Auto2048','Auto2048-可以自动玩的2048-快来玩吧：http://apps.wandoujia.com/apps/com.hanxi.webapp.auto2048/download ','给好友分享');
  };

  document.getElementById('showAdsAppWall').onclick = function(){
      window.android.showAdsAppWall();
  };

  document.getElementById('sourceCode').onclick = function(){
      window.location.href="https://github.com/ov3y/2048-AI";
  };

});
