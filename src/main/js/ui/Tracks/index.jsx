import React, { Fragment, useEffect, useState} from 'react';
import {
    Box,
    Typography,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import api from "../../util/api";
import TrackCard from "./TrackCard";
import CreateTrackButton from "./CreateTrackButton";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import TableHead from "@material-ui/core/TableHead";
import TableBody from "@material-ui/core/TableBody";
import Table from "@material-ui/core/Table";
import {useLocation} from "react-router-dom";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        paddingTop: 16,
        paddingBottom: 16,
        width: '100%',
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    button: {
        display: "flex",
        marginRight: 16,
        marginLeft: 16,

    },
    header: {
        flexGrow: 0,
        width: "100%",
        display: "flex",
        alignItems: "center",
        marginBottom: 32,
        marginTop: 16,
        paddingRight: 16,
        paddingLeft: 16,
    },
    content: {
        flexGrow: 1,
        display: 'flex',
        flexDirection: 'row',
        flexWrap: 'wrap',
        alignItems: 'flex-start',
    },
    name: {
        width: "100%",
        textAlign: "center",
        flexGrow: 1,
        alignItems: "center",
        height: "100%",
        justifyContent: "center",
        display: "flex",
    }
}));

function Tracks() {
    const classes = useStyles();
    const location = useLocation();
    const [tracks, setTracks] = useState();
    const [isDownload, setDownload] = useState(true);
    const [playlistName, setPlaylistName] = useState();

    const handleCreateTrack = (trackId) =>{
        setTracks([...tracks, trackId]);
    }
    useEffect(()=>{
        setDownload(true);
        console.log("location",location.hash);
        api(
            `/api/playlists/get?id=${location.hash.substring(1)}`,
            'GET',
            (data) => {
                console.log("data", data);
                setTracks(data.tracks);
                setDownload(false);
                setPlaylistName(data.name);
            },
            (data) => {
                setDownload(false);
                setTracks(null);
            }
        );
    }, [location.hash]);

    return (
        <Box className={classes.root} >
            {isDownload && (
                <Typography variant="h6" className={classes.name} >
                    Загрузка...
                </Typography>
            )}
            {!isDownload && !tracks && (
                <Typography variant="h6" className={classes.name} >
                    Такого плейлиста нет
                </Typography>
            )}
            {!isDownload && tracks && (
                <Fragment>
                    <Box className={classes.header}>
                        <Typography variant="h2">
                            {playlistName}
                        </Typography>
                        <CreateTrackButton playlistId={location.hash.substring(1)} onCreate={handleCreateTrack}/>
                    </Box>
                    <Box className={classes.content}>
                        { tracks && tracks.length!==0 && (
                            <Table className={classes.table} aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Название</TableCell>
                                        <TableCell >Исполнитель</TableCell>
                                        <TableCell align="right">Размер&nbsp;(Mb)</TableCell>
                                        <TableCell align="right">Длительность&nbsp;(сек)</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {tracks.map((id) => (
                                        <TrackCard id={id} key={id}/>
                                    ))}
                                </TableBody>
                            </Table>
                        )}
                        {!isDownload && tracks && tracks.length===0 && (
                            <Typography variant="h6" className={classes.name}>
                                Треков еще нет
                            </Typography>
                        )}
                    </Box>
                </Fragment>
            )}

        </Box>
    );
}

export default Tracks;