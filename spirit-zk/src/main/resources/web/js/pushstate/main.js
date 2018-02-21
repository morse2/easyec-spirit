$(window).bind("popstate", function(event) {
    var preState = history.state;
    if (preState) {
        zAu.send(
            new zk.Event(null, "onPopState", preState
            )
        );
    }
});