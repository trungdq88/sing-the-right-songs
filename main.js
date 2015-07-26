/**
 * Created by dinhquangtrung on 7/26/15.
 */
var SONGS;
var TITLES;
function log(s) {
    console.log(s);
}
$(document).ready(function () {
    log("Loading...");
    setTimeout(function () {
        log("Reading JSON file...");
        $.ajax({
            dataType: "json",
            url: "songs.json",
            async: false,
            success: function(data) {
                SONGS = data;
            }
        });
        $.ajax({
            dataType: "json",
            url: "song-title.json",
            async: false,
            success: function(data) {
                TITLES = data;
            }
        });

        log(Object.keys(SONGS).length + " songs loaded!");

        $('#content').fadeIn(1000);
        $('#loading').hide();
    }, 1000);
});
function search() {
    var $result = $('#result');
    $result.html('');

    var from = +$('#vocalFrom').val();
    var to = +$('#vocalTo').val();

    // Find the songs
    var goodSongs = [];
    var keys = Object.keys(SONGS);
    for (var iKeys = 0; iKeys < keys.length; iKeys++) {
        var maxDistance = 0;
        var thisSongIsGood = 0;
        var thisSongIsNotGood = 0;
        var songId = keys[iKeys];
        var songNotes = SONGS[songId];
        for (var iNotes = 0; iNotes < songNotes.length; iNotes++) {
            var note = songNotes[iNotes];
            if (note == 0) continue; // Ignore the ending note because it is always 0
            if (note >= from && note <= to) {
                thisSongIsGood++;
            } else {
                thisSongIsNotGood++;
                if (Math.abs(note - from) > maxDistance || Math.abs(note - to) > maxDistance) {
                    if (note < from) {
                        maxDistance = Math.abs(note - from);
                    } else if (note > to) {
                        maxDistance = Math.abs(note - to);
                    }
                }
            }
        }
        goodSongs.push({
            songId: songId,
            goodness: 1 - thisSongIsNotGood / songNotes.length,
            maxDistance: maxDistance
        });
    }

    // Sort the result, sort by goodness ratio (higher is better and listed first)
    goodSongs.sort(function (a, b) {
        return b.goodness - a.goodness
    });

    // Show result
    for (var i = 0; i < goodSongs.length; i++) {
        var song = goodSongs[i];
        var title = TITLES[song.songId];
        $result.append($('<tr><td>' + title + "</td><td>" + song.songId + "</td><td>"
        + Math.round(song.goodness * 100) + "%</td><td>" + song.maxDistance + '</td></tr>'))
    }

}