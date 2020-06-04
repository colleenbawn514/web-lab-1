import { createMuiTheme } from '@material-ui/core/styles';

export default createMuiTheme({
    palette: {
        type: "dark",
        primary: {
            main: '#2675F0',
            contrastText: '#fff',
        },
        secondary: {
            main: '#ffffff',
            contrastText: '#fff',
        },
        background: {
            paper: '#181818',
            default: '#121212'
        }
    },
    overrides: {

    },
});