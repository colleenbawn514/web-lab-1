import React, {useEffect, useState} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import api from "../../util/api";
import {Album} from "@material-ui/icons";
import {Link,  } from "react-router-dom";
const useStyles = makeStyles({
    root: {
        width: 200,
        marginRight: 16,
        marginBottom: 16,
    },
    media: {
        height: 140,
        alignItems: "center",
        justifyContent: "center",
        display: "flex",
        background: 'linear-gradient(106deg, #ff00cc, #333399)',
    },
    icon: {
        width: 70,
        height: 70,
    },
    text:{
        wordBreak: "break-word",
    }
});

export default function MediaCard({id}) {
    const classes = useStyles();
    const [playlist, setPlaylist] = useState();
    const [isDownload, setDownload] = useState();

    useEffect(()=>{
        setDownload(true);
        api(
            `/api/playlists/get?id=${id}`,
            'GET',
            (data) => {
                setPlaylist(data);
                setDownload(false);
            },
            (data) => {
                console.error(data);
            }
        );
    }, []);

    return (
        <Card className={classes.root}>
            <CardActionArea component={Link} to={`/playlist#${id}`}>
                <CardMedia className={classes.media}>
                    <Album className={classes.icon}/>
                </CardMedia>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="h2" className={classes.text}>
                        {!isDownload && playlist && playlist.name || "Загрузка..."}
                    </Typography>
                    <Typography variant="body2" color="textSecondary" component="p" className={classes.text}>
                        Количество треков: {!isDownload && playlist && playlist.tracks.length+"" || "Загрузка..."}
                    </Typography>
                </CardContent>
            </CardActionArea>
        </Card>
    );
}
