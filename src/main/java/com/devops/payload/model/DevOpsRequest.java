package com.devops.payload.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DevOpsRequest {

    @NotBlank(message = "Message cannot be blank")
    private String message;

    @NotBlank(message = "To field cannot be blank")
    private String to;

    @NotBlank(message = "From field cannot be blank")
    private String from;

    @NotNull(message = "TimeToLifeSec cannot be null")
    @Positive(message = "TimeToLifeSec must be positive")
    private Integer timeToLifeSec;

    public DevOpsRequest() {
    }

    public DevOpsRequest(String message, String to, String from, Integer timeToLifeSec) {
        this.message = message;
        this.to = to;
        this.from = from;
        this.timeToLifeSec = timeToLifeSec;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getTimeToLifeSec() {
        return timeToLifeSec;
    }

    public void setTimeToLifeSec(Integer timeToLifeSec) {
        this.timeToLifeSec = timeToLifeSec;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((timeToLifeSec == null) ? 0 : timeToLifeSec.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevOpsRequest other = (DevOpsRequest) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (to == null) {
            if (other.to != null)
                return false;
        } else if (!to.equals(other.to))
            return false;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (timeToLifeSec == null) {
            if (other.timeToLifeSec != null)
                return false;
        } else if (!timeToLifeSec.equals(other.timeToLifeSec))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DevOpsRequest [message=" + message + ", to=" + to + ", from=" + from + ", timeToLifeSec="
                + timeToLifeSec + "]";
    }

}

