import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
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
    input: {
        marginBottom: 16,
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
export default function CreatePlaylistButton({playlistId, onCreate}) {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [name, setName] = useState("");
    const [author, setAuthor] = useState("");
    const [size, setSize] = useState("");
    const [duration, setDuration] = useState("");
    const [isSave, setIsSave] = useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const handleCreate = () => {
        setIsSave(true);
        api(
            `/api/tracks/create?name=${name}&size=${size}&duration=${duration}&author=${author}&playlistId=${playlistId}`,
            'POST',
            (data) => {
                setIsSave(false);
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
                <DialogTitle id="form-dialog-title">Создание трека</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        label="Название"
                        variant="outlined"
                        fullWidth
                        className={classes.input}
                        onChange={(event)=>{
                            setName(event.target.value);
                        }}
                        disabled={isSave}
                    />
                    <TextField
                        label="Исполнитель"
                        variant="outlined"
                        fullWidth
                        className={classes.input}
                        onChange={(event)=>{
                            setAuthor(event.target.value);
                        }}
                        disabled={isSave}
                    />
                    <TextField
                        label="Размер (Mb)"
                        variant="outlined"
                        fullWidth
                        type="number"
                        step="0.01"
                        className={classes.input}
                        onChange={(event)=>{
                            setSize(event.target.value);
                        }}
                        disabled={isSave}
                    />
                    <TextField
                        label="Длительность (сек)"
                        variant="outlined"
                        fullWidth
                        type="number"
                        onChange={(event)=>{
                            setDuration(event.target.value);
                        }}
                        disabled={isSave}
                    />

                </DialogContent>
                <DialogActions >
                    <Button onClick={handleClose} disabled={isSave}>
                        Отмена
                    </Button>
                    <div className={classes.wrapper}>
                        <Button
                            onClick={handleCreate}
                            color="primary"
                            disabled={
                                name.trim()===""
                                || author.trim()===""
                                || size.trim()===""
                                || duration.trim()===""
                                || isSave
                            }
                        >
                            Создать трек
                        </Button>
                        {isSave && <CircularProgress size={24} className={classes.buttonProgress} />}
                    </div>
                </DialogActions>
            </Dialog>
        </div>
    );
}
