import React, {useState} from 'react';
import { Box, CardMedia } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import Typography from "@material-ui/core/Typography";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        width: '100%',
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
    media: {
        width: 512,
        height: 512,

    },
    container: {
        display: 'flex',
        flexDirection: "column",
        alignItems: 'center',
        justifyContent: 'center',
    }
}));

function Home() {
    const classes = useStyles();
    const [login, setLogin] = useState();
    const [password, setPassword] = useState();
    const [error, setError] = useState(false);

    return (
        <Box className={classes.root} >
            <Box className={classes.container}>
                <Typography variant="h3">
                    Добро пожаловать в Разочарование
                </Typography>
                <CardMedia image="http://localhost:8080/static/images/1-13646-512.png" className={classes.media}/>
            </Box>
        </Box>
    );
}

export default Home;