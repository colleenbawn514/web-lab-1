import React, { Fragment, useEffect, useState} from 'react';
import {
    Box,
    Typography,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import api from "../../util/api";
import PlaylistCard from "./PlaylistCard";
import CreatePlaylistButton from "./CreatePlaylistButton";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        padding: 16,
        paddingRight: 0,
        paddingBottom: 0,
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
    },
    content: {
        flexGrow: 1,
        display: 'flex',
        flexDirection: 'row',
        flexWrap: 'wrap',
        alignItems: 'flex-start',
    }
}));

function Playlists() {
    const classes = useStyles();
    const [playlists, setPlaylists] = useState();
    const [isDownload, setDownload] = useState(true);

    const handleCreatePlaylist = (playlistId) =>{
        setPlaylists([...playlists, playlistId]);
    }

    useEffect(()=>{
        setDownload(true);
        api(
            `/api/users/get?id=${localStorage.getItem("userId")}`,
            'GET',
            (data) => {
                setPlaylists(data.playlists);
                setDownload(false);
            },
            (data) => {
                console.error(data);
            }
        );
    }, []);

    return (
        <Box className={classes.root} >
            <Box className={classes.header}>
                <Typography variant="h2">
                    Плейлисты
                </Typography>
                <CreatePlaylistButton onCreate={ handleCreatePlaylist}/>
            </Box>
            <Box className={classes.content}>
                {isDownload && (
                    <Fragment>
                        <Typography variant="h6" className={classes.name} >
                            Загрузка...
                        </Typography>
                    </Fragment>
                )}
                {!isDownload && playlists && playlists.length!==0 &&(
                    <Fragment>

                        {playlists.map((playlistId)=>(
                            <PlaylistCard id={playlistId} key={playlistId}/>
                        ))}
                    </Fragment>
                )}
                {!isDownload && playlists && playlists.length===0 && (
                    <Typography variant="h6">
                        Плейлистов еще нет
                    </Typography>
                )}
            </Box>

        </Box>
    );
}

export default Playlists;