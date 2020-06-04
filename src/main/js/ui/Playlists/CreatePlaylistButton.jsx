import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {Add} from "@material-ui/icons";
import {makeStyles} from "@material-ui/core/styles";
import api from "../../util/api";
import CircularProgress from "@material-ui/core/CircularProgress";

const useStyles = makeStyles((theme) => ({
    button: {
        display: "flex",
        marginRight: 16,
        marginLeft: 16,
    },
    buttonProgress: {
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -12,
        marginLeft: -12,
    },
    wrapper: {
        position: 'relative',
    },
}));
export default function CreatePlaylistButton({onCreate}) {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [name, setName] = useState("");
    const [isSave, setIsSave] = useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
     const handleCreate = () => {
         api(
             `/api/playlists/create?name=${name}&userId=${localStorage.getItem("userId")}`,
             'POST',
             (data) => {
                 onCreate(data.id);
                 setOpen(false);
             },
             (data) => {
                 setIsSave(false);
                 console.error(data);
             }
         );
     };
    return (
        <div>
            <Button
                variant="outlined"
                color="primary"
                className={classes.button}
                startIcon={<Add/>}
                onClick={handleClickOpen}
            >
                Добавить
            </Button>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title" fullWidth="sm">
                <DialogTitle id="form-dialog-title">Создание плейлиста</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        label="Название"
                        variant="outlined"
                        fullWidth
                        disabled={isSave}
                        onChange={(event)=>{
                            setName(event.target.value);
                        }}
                    />
                </DialogContent>
                <DialogActions >
                    <Button onClick={handleClose} disabled={isSave} >
                        Отмена
                    </Button>
                    <div className={classes.wrapper}>
                        <Button
                            onClick={handleCreate}
                            color="primary"
                            disabled={
                                name.trim()===""
                                || isSave
                            }
                        >
                            Создать плейлист
                        </Button>
                        {isSave && <CircularProgress size={24} className={classes.buttonProgress} />}
                    </div>
                </DialogActions>
            </Dialog>
        </div>
    );
}
