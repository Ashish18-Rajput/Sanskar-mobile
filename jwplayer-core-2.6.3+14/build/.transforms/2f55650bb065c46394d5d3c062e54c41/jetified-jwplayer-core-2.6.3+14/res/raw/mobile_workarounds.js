// This file contains workarounds for running JW Player in a WebView on Android

// Null out fullscreen functions - these are used by the webplayer to determine whether it should do fullscreen handling via DOM or via provider.setFullscreen()
// We want to use provider.setFullscreen() because on Android < 4.4 the DOM fullscreen functions are not available and we want to have a consistent 
// implementation for all versions
document.webkitExitFullscreen = null;
document.webkitCancelFullScreen = null;