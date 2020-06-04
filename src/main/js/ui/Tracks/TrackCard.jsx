import React, {useEffect, useState} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import api from "../../util/api";
import {TableCell, TableRow} from "@material-ui/core";

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
    const [track, setTrack] = useState();
    const [isDownload, setDownload] = useState();

    useEffect(()=>{
        setDownload(true);
        api(
            `/api/tracks/get?id=${id}`,
            'GET',
            (data) => {
                setTrack(data);
                setDownload(false);
            },
            (data) => {
                console.error(data);
            }
        );
    }, []);

    return (
        <TableRow>
            <TableCell component="th" scope="row">
                {!isDownload && track && track.name || "Загрузка..."}
            </TableCell>
            <TableCell >{ track && track.author}</TableCell>
            <TableCell align="right">{ track && track.size}</TableCell>
            <TableCell align="right">{ track && track.duration}</TableCell>
        </TableRow>
    );
}
