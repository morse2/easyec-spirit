$(window).bind("popstate", function(event) {
    var preState = event.originalEvent.state;
    if (preState) {
        zAu.send(
            new zk.Event(null, "onPopState", preState
            )
        );
    }
});